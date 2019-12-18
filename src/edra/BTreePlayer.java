package edra;


import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.MatchPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.Selector;
import edra.behavior_tree.Sequence;
import edra_nodes.conditions.IfBallIsInGoalieArea;
import edra_nodes.conditions.IfBallIsInFieldAttack;
import edra_nodes.conditions.IfBallIsInFieldDefense;
import edra_nodes.conditions.IfBallIsInMiddle;
import edra_nodes.conditions.IfBallIsInSmallArea;
import edra_nodes.conditions.IfClosestPlayerToBall;
import edra_nodes.conditions.IfIAmNotGoalie;
import edra_nodes.conditions.IfLeftSide;
import edra_nodes.conditions.IfPlayerIsInFieldAttack;
import edra_nodes.conditions.IfPlayerIsInFieldDefense;
import edra_nodes.conditions.IfPlayerIsInMiddle;
import edra_nodes.conditions.IfRightSide;
import edra_nodes.movements.MovePlayerToHomePosition;
import edra_nodes.movements.MovePlayerToKickOff;
import edra_nodes.movements.MovePlayerToSmallArea;
import edra_nodes.states.AfterGoalLeft;
import edra_nodes.states.AfterGoalRight;
import edra_nodes.states.BeforeKickOff;
import edra_nodes.states.CornerKickLeft;
import edra_nodes.states.CornerKickRight;
import edra_nodes.states.FreeKickLeft;
import edra_nodes.states.FreeKickRight;
import edra_nodes.states.KickInLeft;
import edra_nodes.states.KickInRight;
import edra_nodes.states.KickOffLeft;
import edra_nodes.states.KickOffRight;
import edra_nodes.states.OffsideLeft;
import edra_nodes.states.OffsideRight;
import edra_nodes.states.PlayOn;
import edra_team.actions.AdvanceAccordingToHomePosition;
import edra_team.actions.AdvanceWithBallToGoal;
import edra_team.actions.GoGetBall;
import edra_team.actions.KickToScore;
import edra_team.actions.MoveAccordingToBall;
import edra_team.actions.PassBall;
import edra_team.actions.RetreatAccordingToHomePosition;
import edra_team.actions.ReturnToHomePosition;


	public class BTreePlayer extends Thread {
		private final PlayerCommander commander;
		private PlayerPerception selfPerc;
		private FieldPerception fieldPerc;
		private MatchPerception matchPerc;
		private Vector2D homePosition;
		private Vector2D goalPosition;
		private BTNode<BTreePlayer> btree;

		//int LOOP_INTERVAL = 100;
		
		public BTreePlayer(PlayerCommander player, Vector2D home) {
			commander = player;
			homePosition = home;

		//	btree = buildTree();
		}
		
		@Override
		public void run() {
			//long nextIteration = System.currentTimeMillis() + LOOP_INTERVAL;
			System.out.println(">> 1. Waiting initial perceptions...");
			updatePerceptionsBlocking();
			//selfPerc = commander.perceiveSelfBlocking();
			//fieldPerc = commander.perceiveFieldBlocking();

			System.out.println(">> 2. Moving to initial position...");
			commander.doMoveBlocking(homePosition.getX(), homePosition.getY());
			updatePerceptionsBlocking();

			//selfPerc = commander.perceiveSelfBlocking();
			//fieldPerc = commander.perceiveFieldBlocking();

			if (selfPerc.getSide() == EFieldSide.LEFT) {
				goalPosition = new Vector2D(52.0d, 0);
			} else {
				goalPosition = new Vector2D(-52.0d, 0);
				homePosition.setX(-homePosition.getX()); // inverte, porque somente no move as coordenadas sao
															// espelhadas
															// independente de lado
				homePosition.setY(-homePosition.getY());
			}

			System.out.println(">> 3. Now starting...");

			switch (selfPerc.getUniformNumber()) {
			case 1:
				btree = buildTree(Goleiro());
				break;
			case 2:
				btree = buildTree(Meia());
				break;
			case 3:
				btree = buildTree(ZagaDireita());
				break;
			case 4:
				btree = buildTree(ZagaEsquerda());
				break;
			case 5:
				btree = buildTree(LateralDireito());
				break;
			case 6:
				btree = buildTree(LateralEsquerdo());
				break;
			case 7:
				btree = buildTree(AtacDireito());
				break;
			case 8:
				btree = buildTree(AtacEsquerdo());
				break;
			default:
				break;
			}

			while (commander.isActive()) {

				btree.tick(this);

				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				updatePerceptions(); // non-blocking
			}

			System.out.println(">> 4. Terminated!");
		}
		
		

		// CONSTRUINDO A ARVORE
		//Raiz
		private BTNode<BTreePlayer> buildTree(BTNode<BTreePlayer> playerBehaviorTree) {

			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");
			raiz.add(beforeKickOff_afterGoal());	
			raiz.add(freeKick());					
			raiz.add(kickOff());					
			raiz.add(offside()); 					
			raiz.add(cornerKick());					
			raiz.add(kickIn());						
			raiz.add(playerBehaviorTree);
			
			return raiz;
			
		}
		// Inicio do jogo
		private BTNode<BTreePlayer> playOn(BTNode<BTreePlayer> playerBehaviorTree) {
			
			Selector<BTreePlayer> playOn = new Selector<BTreePlayer>("Play On");
			playOn.add(new PlayOn());
			playOn.add(playerBehaviorTree);  //adicionar behavior tree padrao dos jogadores
			
			return playOn;
			
		}
		
		private BTNode<BTreePlayer> beforeKickOff_afterGoal() {
			Selector<BTreePlayer> root = new Selector<BTreePlayer>("BEFORE KICK OFF / AFTER GOAL");
			Sequence<BTreePlayer> beforeKickOff = new Sequence<BTreePlayer>("BEFORE KICK OFF");
			Sequence<BTreePlayer> afterGoalLeft = new Sequence<BTreePlayer>("AFTER GOAL LEFT");
			Sequence<BTreePlayer> afterGoalRight = new Sequence<BTreePlayer>("AFTER GOAL RIGHT");

			root.add(beforeKickOff);
			root.add(afterGoalLeft);
			root.add(afterGoalRight);

			beforeKickOff.add(new BeforeKickOff());
			beforeKickOff.add(new MovePlayerToHomePosition());

			afterGoalLeft.add(new AfterGoalLeft());
			afterGoalLeft.add(new MovePlayerToHomePosition());

			afterGoalRight.add(new AfterGoalRight());
			afterGoalRight.add(new MovePlayerToHomePosition());

			return root;
			
		}
		
		private BTNode<BTreePlayer> freeKick() {
			Selector<BTreePlayer> root = new Selector<BTreePlayer>("FREE KICK");
			Sequence<BTreePlayer> freeKickLeft = new Sequence<BTreePlayer>("FREE KICK LEFT?");
			Sequence<BTreePlayer> freeKickRight = new Sequence<BTreePlayer>("FREE KICK RIGHT?");

			root.add(freeKickLeft);
			root.add(freeKickLeft);

			freeKickLeft.add(new FreeKickLeft());
			freeKickLeft.add(new GoGetBall());
			freeKickLeft.add(new KickToScore());

			freeKickRight.add(new FreeKickRight());
			freeKickRight.add(new GoGetBall());
			freeKickRight.add(new KickToScore());

			return root;
			
		}
		
		private BTNode<BTreePlayer> kickOff() {
			Selector<BTreePlayer> root = new Selector<BTreePlayer>("BEFORE KICK OFF / AFTER GOAL");
			Sequence<BTreePlayer> kickOffLeft = new Sequence<BTreePlayer>("KICK OFF LEFT");
			Sequence<BTreePlayer> kickOffRight = new Sequence<BTreePlayer>("KICK OFF RIGHT");

			root.add(kickOffLeft);
			root.add(kickOffRight);

			kickOffLeft.add(new KickOffLeft());
			kickOffLeft.add(new MovePlayerToKickOff());
			kickOffLeft.add(new IfClosestPlayerToBall());
			kickOffLeft.add(new GoGetBall());
			kickOffLeft.add(new PassBall());

			kickOffRight.add(new KickOffRight());
			kickOffRight.add((new MovePlayerToKickOff()));
			kickOffRight.add(new IfClosestPlayerToBall());
			kickOffRight.add(new GoGetBall());
			kickOffRight.add(new PassBall());

			return root;
			
		}
		
		//Impedimento
		private BTNode<BTreePlayer> offside() {
			Selector<BTreePlayer> offside = new Selector<BTreePlayer>("CHECANDO IMPEDIMENTO");
			Sequence<BTreePlayer> offsideLeft = new Sequence<BTreePlayer>("CHECANDO IMPEDIMENTO ESQUERDA");
			Sequence<BTreePlayer> offsideRight = new Sequence<BTreePlayer>("CHECANDO IMPEDIMENTO DIREITA");

			offside.add(offsideLeft);
			offside.add(offsideRight);

			//Se Esta impedido, recue
			offsideLeft.add(new OffsideLeft());
			offsideLeft.add(new IfLeftSide());
			offsideLeft.add(new IfIAmNotGoalie());
			offsideLeft.add(new RetreatAccordingToHomePosition());
			
			//Senao passe a bola
			offsideLeft.add(new IfClosestPlayerToBall());
			offsideLeft.add(new PassBall());

			//Se Esta impedido, recue
			offsideRight.add(new OffsideRight());
			offsideRight.add(new IfRightSide());
			offsideRight.add(new IfIAmNotGoalie());
			offsideRight.add(new RetreatAccordingToHomePosition());
			
			//Senao passe a bola
			offsideRight.add(new IfClosestPlayerToBall());
			offsideRight.add(new PassBall());

			return offside;
			
		}
		
		private BTNode<BTreePlayer> cornerKick(){ //TODO
			Selector<BTreePlayer> cornerKick = new Selector<BTreePlayer>("Escanteio");
			Sequence<BTreePlayer> cornerKickLeft = new Sequence<BTreePlayer>("Escanteio esquerda");
			Sequence<BTreePlayer> cornerKickRight = new Sequence<BTreePlayer>("Escanteio direita");
			cornerKick.add(cornerKickLeft);
			cornerKick.add(cornerKickRight);
			
			cornerKickLeft.add(new CornerKickLeft()); //52x,34y -34y 
			cornerKickLeft.add(new IfClosestPlayerToBall());
			cornerKickLeft.add(new MovePlayerToSmallArea());
			cornerKickLeft.add(new PassBall());
			
			cornerKickRight.add(new CornerKickRight()); //-52x,34y -34y 
			cornerKickRight.add(new IfClosestPlayerToBall());
			cornerKickRight.add(new MovePlayerToSmallArea());
			cornerKickRight.add(new PassBall());
			
			return cornerKick;
			
		}
		
		private BTNode<BTreePlayer> kickIn(){ //TODO
			Selector<BTreePlayer> kickIn = new Selector<BTreePlayer>("Lateral");
			Sequence<BTreePlayer> kickInLeft = new Sequence<BTreePlayer>("Lateral esquerda"); //esquerda cobrar
			Sequence<BTreePlayer> kickInRight = new Sequence<BTreePlayer>("Lateral direita"); //direita cobrar
			kickIn.add(kickInLeft);
			kickIn.add(kickInRight);
			
			kickInLeft.add(new KickInLeft()); //52x,34y -34y 
			kickInLeft.add(new PassBall());
			
			kickInRight.add(new KickInRight()); //-52x,34y -34y 
			kickInRight.add(new PassBall());
			
			return kickIn;
			
		}
		
		private BTNode<BTreePlayer> Goleiro() { //Completo
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("GOLEIRO");

			Sequence<BTreePlayer> bolaNaPequenaArea = new Sequence<BTreePlayer>("BOLA ESTA NA PEQUENA AREA?");
			bolaNaPequenaArea.add(new IfBallIsInSmallArea());
			bolaNaPequenaArea.add(new KickToScore());

			Sequence<BTreePlayer> bolaNaGrandeArea = new Sequence<BTreePlayer>("BOLA ESTA NA GRANDE AREA?");
			bolaNaGrandeArea.add(new IfBallIsInGoalieArea());
			bolaNaGrandeArea.add(new GoGetBall());
			bolaNaGrandeArea.add(new KickToScore());

			raiz.add(bolaNaPequenaArea);
			raiz.add(bolaNaGrandeArea);
			raiz.add(new MoveAccordingToBall());
			
			return raiz;
			
		}
		
		private BTNode<BTreePlayer> Meia() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");

			Sequence<BTreePlayer> ataque = new Sequence<BTreePlayer>("ATACANDO");
			ataque.add(new AdvanceWithBallToGoal());
			ataque.add(new KickToScore());

			Sequence<BTreePlayer> defender = new Sequence<BTreePlayer>("DEFENDENDO");
			defender.add(new IfClosestPlayerToBall());
			defender.add(new GoGetBall());

			raiz.add(ataque);
			raiz.add(defender);
			
			return raiz;
			
		}
		
		private BTNode<BTreePlayer> LateralDireito() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");


			Selector<BTreePlayer> regiaoMeio = new Selector<BTreePlayer>("REGIAO DO MEIO");

			Sequence<BTreePlayer> d = new Sequence<BTreePlayer>(" ");
			d.add(new IfPlayerIsInMiddle());
			d.add(new IfBallIsInMiddle());
			d.add(new GoGetBall());
			d.add(new IfClosestPlayerToBall());
			d.add(new PassBall());

			Sequence<BTreePlayer> e = new Sequence<BTreePlayer>(" ");
			e.add(new IfPlayerIsInMiddle());
			e.add(new IfBallIsInFieldDefense());
			e.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> f = new Sequence<BTreePlayer>(" ");
			f.add(new IfPlayerIsInMiddle());
			f.add(new IfBallIsInFieldAttack());
			f.add(new RetreatAccordingToHomePosition());

			regiaoMeio.add(d);
			regiaoMeio.add(e);
			regiaoMeio.add(f);


			Selector<BTreePlayer> regiaoAtaque = new Selector<BTreePlayer>("REGIAO DO MEIO");

			Sequence<BTreePlayer> g = new Sequence<BTreePlayer>(" ");
			g.add(new IfPlayerIsInFieldAttack());
			g.add(new IfBallIsInFieldAttack());
			g.add(new IfClosestPlayerToBall());
			g.add(new KickToScore());

			Sequence<BTreePlayer> h = new Sequence<BTreePlayer>(" ");
			h.add(new IfPlayerIsInFieldAttack());
			h.add(new IfBallIsInFieldAttack());
			h.add(new GoGetBall());
			h.add(new PassBall());

			Sequence<BTreePlayer> i = new Sequence<BTreePlayer>(" ");
			i.add(new IfPlayerIsInFieldAttack());
			i.add(new IfBallIsInMiddle());
			i.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> j = new Sequence<BTreePlayer>(" ");
			j.add(new IfPlayerIsInFieldAttack());
			j.add(new IfBallIsInMiddle());
			j.add(new ReturnToHomePosition());

			regiaoAtaque.add(g);
			regiaoAtaque.add(h);
			regiaoAtaque.add(i);
			regiaoAtaque.add(j);

			raiz.add(regiaoMeio);
			raiz.add(regiaoAtaque);
//			raiz.add(regiaoDefesa);
			raiz.add(new ReturnToHomePosition());
			
			return raiz;

		}
		
		private BTNode<BTreePlayer> ZagaDireita() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");

			Selector<BTreePlayer> regiaoDefesa = new Selector<BTreePlayer>("REGIAO DE DEFESA");
			
			//CASO A BOLA ESTEJA NO CAMPO DE DEFESA
			Sequence<BTreePlayer> a = new Sequence<BTreePlayer>(" ");
			a.add(new IfPlayerIsInFieldDefense());
			a.add(new IfBallIsInFieldDefense());
			a.add(new GoGetBall());
			a.add(new IfClosestPlayerToBall());
			a.add(new PassBall());
			
			//CASO A BOLA ESTEJA NO CENTRO DO MAPA
			Sequence<BTreePlayer> b = new Sequence<BTreePlayer>(" ");
			b.add(new IfPlayerIsInFieldDefense());
			b.add(new IfBallIsInMiddle());
			b.add(new AdvanceAccordingToHomePosition());

			//CASO A BOLA ESTEJA NO CAMPO DE ATAQUE
			Sequence<BTreePlayer> c = new Sequence<BTreePlayer>(" ");
			c.add(new IfPlayerIsInFieldDefense());
			c.add(new IfBallIsInFieldAttack());
			c.add(new ReturnToHomePosition());

			regiaoDefesa.add(a);
			regiaoDefesa.add(b);
			regiaoDefesa.add(c);

			Selector<BTreePlayer> regiaoMeio = new Selector<BTreePlayer>("REGIAO DO MEIO");
			
			//
			Sequence<BTreePlayer> d = new Sequence<BTreePlayer>(" ");
			d.add(new IfPlayerIsInMiddle());
			d.add(new IfBallIsInMiddle());
			d.add(new GoGetBall());
			d.add(new IfClosestPlayerToBall());
			d.add(new PassBall());

			Sequence<BTreePlayer> e = new Sequence<BTreePlayer>(" ");
			e.add(new IfPlayerIsInMiddle());
			e.add(new IfBallIsInFieldDefense());
			e.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> f = new Sequence<BTreePlayer>(" ");
			f.add(new IfPlayerIsInMiddle());
			f.add(new IfBallIsInFieldAttack());
			f.add(new RetreatAccordingToHomePosition());

			regiaoMeio.add(d);
			regiaoMeio.add(e);
			regiaoMeio.add(f);

			raiz.add(regiaoDefesa);
			raiz.add(regiaoMeio);
			raiz.add(new ReturnToHomePosition());
			
			return raiz;
		
		}
		
		private BTNode<BTreePlayer> AtacDireito() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");

			Sequence<BTreePlayer> ataque = new Sequence<BTreePlayer>(" ");
			ataque.add(new AdvanceWithBallToGoal());
			ataque.add(new KickToScore());

			Sequence<BTreePlayer> defender = new Sequence<BTreePlayer>(" ");
			defender.add(new IfClosestPlayerToBall());
			defender.add(new GoGetBall());

			raiz.add(ataque);
			raiz.add(defender);
			
			return raiz;
			
		}
		
		private BTNode<BTreePlayer> LateralEsquerdo() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");

			Selector<BTreePlayer> regiaoMeio = new Selector<BTreePlayer>("REGIAO DO MEIO");

			Sequence<BTreePlayer> d = new Sequence<BTreePlayer>(" ");
			d.add(new IfPlayerIsInMiddle());
			d.add(new IfBallIsInMiddle());
			d.add(new GoGetBall());
			d.add(new IfClosestPlayerToBall());
			d.add(new PassBall());

			Sequence<BTreePlayer> e = new Sequence<BTreePlayer>(" ");
			e.add(new IfPlayerIsInMiddle());
			e.add(new IfBallIsInFieldDefense());
			e.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> f = new Sequence<BTreePlayer>(" ");
			f.add(new IfPlayerIsInMiddle());
			f.add(new IfBallIsInFieldAttack());
			f.add(new RetreatAccordingToHomePosition());

			regiaoMeio.add(d);
			regiaoMeio.add(e);
			regiaoMeio.add(f);

			Selector<BTreePlayer> regiaoAtaque = new Selector<BTreePlayer>("REGIAO DO MEIO");

			Sequence<BTreePlayer> g = new Sequence<BTreePlayer>(" ");
			g.add(new IfPlayerIsInFieldAttack());
			g.add(new IfBallIsInFieldAttack());
			g.add(new IfClosestPlayerToBall());
			g.add(new KickToScore());

			Sequence<BTreePlayer> h = new Sequence<BTreePlayer>(" ");
			h.add(new IfPlayerIsInFieldAttack());
			h.add(new IfBallIsInFieldAttack());
			h.add(new GoGetBall());
			h.add(new PassBall());

			Sequence<BTreePlayer> i = new Sequence<BTreePlayer>(" ");
			i.add(new IfPlayerIsInFieldAttack());
			i.add(new IfBallIsInMiddle());
			i.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> j = new Sequence<BTreePlayer>(" ");
			j.add(new IfPlayerIsInFieldAttack());
			j.add(new IfBallIsInMiddle());
			j.add(new ReturnToHomePosition());

			regiaoAtaque.add(g);
			regiaoAtaque.add(h);
			regiaoAtaque.add(i);
			regiaoAtaque.add(j);

			raiz.add(regiaoMeio);
			raiz.add(regiaoAtaque);
//			raiz.add(regiaoDefesa);
			raiz.add(new ReturnToHomePosition());
			
			return raiz;
			
		}

		private BTNode<BTreePlayer> ZagaEsquerda() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");

			Selector<BTreePlayer> regiaoDefesa = new Selector<BTreePlayer>("REGIAO DE DEFESA");

			Sequence<BTreePlayer> a = new Sequence<BTreePlayer>(" ");
			a.add(new IfPlayerIsInFieldDefense());
			a.add(new IfBallIsInFieldDefense());
			a.add(new GoGetBall());
			a.add(new IfClosestPlayerToBall());
			a.add(new PassBall());

			Sequence<BTreePlayer> b = new Sequence<BTreePlayer>(" ");
			b.add(new IfPlayerIsInFieldDefense());
			b.add(new IfBallIsInMiddle());
			b.add(new AdvanceAccordingToHomePosition());

			Sequence<BTreePlayer> c = new Sequence<BTreePlayer>(" ");
			c.add(new IfPlayerIsInFieldDefense());
			c.add(new IfBallIsInFieldAttack());
			c.add(new ReturnToHomePosition());

			regiaoDefesa.add(a);
			regiaoDefesa.add(b);
			regiaoDefesa.add(c);

			Selector<BTreePlayer> regiaoMeio = new Selector<BTreePlayer>("REGIAO DO MEIO");

			Sequence<BTreePlayer> d = new Sequence<BTreePlayer>(" ");
			d.add(new IfPlayerIsInMiddle());
			d.add(new IfBallIsInMiddle());
			d.add(new GoGetBall());
			d.add(new IfClosestPlayerToBall());
			d.add(new PassBall());

			Sequence<BTreePlayer> e = new Sequence<BTreePlayer>(" ");
			e.add(new IfPlayerIsInMiddle());
			e.add(new IfBallIsInFieldDefense());
			e.add(new RetreatAccordingToHomePosition());

			Sequence<BTreePlayer> f = new Sequence<BTreePlayer>(" ");
			f.add(new IfPlayerIsInMiddle());
			f.add(new IfBallIsInFieldAttack());
			f.add(new RetreatAccordingToHomePosition());

			regiaoMeio.add(d);
			regiaoMeio.add(e);
			regiaoMeio.add(f);

			raiz.add(regiaoDefesa);
			raiz.add(regiaoMeio);
			raiz.add(new ReturnToHomePosition());
			
			return raiz;
			
		}

		private BTNode<BTreePlayer> AtacEsquerdo() {
			Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");


			Sequence<BTreePlayer> ataque = new Sequence<BTreePlayer>(" ");
			ataque.add(new AdvanceWithBallToGoal());
			ataque.add(new KickToScore());

			Sequence<BTreePlayer> defender = new Sequence<BTreePlayer>(" ");
			defender.add(new IfClosestPlayerToBall());
			defender.add(new GoGetBall());

			raiz.add(ataque);
			raiz.add(defender);
			
			return raiz;
		
		}
		
			
		
		/*
			Sequence<BTreePlayer> attackTree = new Sequence<BTreePlayer>("Avanca-para-Gol");

			Selector<BTreePlayer> haveTheBall = new Selector<BTreePlayer>("Tenho-a-bola?");
			Sequence<BTreePlayer> goToGoal = new Sequence<BTreePlayer>("Jogada-para-Gol");

			attackTree.add(new IfClosestPlayerToBall());
			attackTree.add(new AdvanceWithBallToGoal());
			attackTree.add(new KickToScore());

			Sequence<BTreePlayer> deffensiveTree = new Sequence<BTreePlayer>("Rouba-Bola");

			deffensiveTree.add(new IfClosestPlayerToBall());
			System.out.println("teste1");
			deffensiveTree.add(new GoGetBall());
			System.out.println("teste2");
			deffensiveTree.add(new Defensores());
			System.out.println("teste3");

			/*
			 * Sequence<BTreePlayer> goleiroTree = new Sequence<BTreePlayer>("GOLEIRO");
			 * goleiroTree.add(acaoGoleiro(null));
			 */

			// BTNode<BTreePlayer> defaultTree = new ReturnToHome("RAIZ"); //fica como
			// EXERCICIO

			/*raiz.add(attackTree);
			raiz.add(deffensiveTree);
			// raiz.add(defaultTree);

			return raiz;
		}
	
	*/
	
		
		private void updatePerceptionsBlocking() {
			PlayerPerception newSelf = commander.perceiveSelfBlocking();
			FieldPerception newField = commander.perceiveFieldBlocking();
			MatchPerception newMatch = commander.perceiveMatchBlocking();
			if (newSelf != null) {
				this.selfPerc = newSelf;
			}
			if (newField != null) {
				this.fieldPerc = newField;
			}
			if (newMatch != null) {
				this.matchPerc = newMatch;
			}
		}

		
		private void updatePerceptions() {
			PlayerPerception newSelf = commander.perceiveSelf();
			FieldPerception newField = commander.perceiveField();
			MatchPerception newMatch = commander.perceiveMatch();

			if (newSelf != null) {
				this.selfPerc = newSelf;
			}
			if (newField != null) {
				this.fieldPerc = newField;
			}
			if (newMatch != null) {
				this.matchPerc = newMatch;
			}
		}

		/**
		 * Algumas funcoes auxiliares que mais de um tipo de no da arvore pode precisar
		 **/

		public boolean closeTo(Vector2D pos) {
			return isCloseTo(pos, 1.5);
		}

		public boolean isCloseTo(Vector2D pos, double minDistance) {
			Vector2D myPos = selfPerc.getPosition();
			return pos.distanceTo(myPos) < minDistance;
		}

		public boolean isAlignedTo(Vector2D position) {
			return isAlignedTo(position, 12.0);
		}

		public boolean isAlignedTo(Vector2D position, double minAngle) {
			if (minAngle < 0)
				minAngle = -minAngle;

			Vector2D myPos = selfPerc.getPosition();

			if (position == null || myPos == null) {
				return false;
			}

			double angle = selfPerc.getDirection().angleFrom(position.sub(myPos));
			return angle < minAngle && angle > -minAngle;
		}

	

		public PlayerCommander getCommander() {
			return commander;
		}

		public PlayerPerception getSelfPerc() {
			return selfPerc;
		}

		public FieldPerception getFieldPerc() {
			return fieldPerc;
		}

		public MatchPerception getMatchPerc() {
			return matchPerc;
		}

		public Vector2D getHomePosition() {
			return homePosition;
		}

		public Vector2D getGoalPosition() {
			return goalPosition;
		}

		public BTNode<BTreePlayer> getBtree() {
			return btree;

		}

	}

