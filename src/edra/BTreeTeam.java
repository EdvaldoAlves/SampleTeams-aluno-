package edra;


import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;


/**
 * Time simples, demonstrado em sala.
 */
public class BTreeTeam extends AbstractTeam {

	public BTreeTeam(String suffix) {
		super(suffix, 8, true);
	}

	@Override
	protected void launchPlayer(int ag, PlayerCommander commander) {
		Vector2D posicaoIni = null;
		//double x, y;

		switch (ag) {
				
		case 0: //GOLEIRO
			posicaoIni = new Vector2D(-49.0d, 0.0d); break;
		case 1: //MEIA
			posicaoIni = new Vector2D(-33.0d, -7.0d); break;
		case 2: //ZAGUEIRO_DIREITO
			posicaoIni = new Vector2D(-33.0d, 7.0d); break;
		case 3: //ZAGUEIRO_ESQUERDO
			posicaoIni = new Vector2D(-23.0d, -18.0d); break;
		case 4: //LATERAL_DIREITO
			posicaoIni = new Vector2D(-23.0d, 18.0d); break;
		case 5: //LATERAL_ESQUERDO
			posicaoIni = new Vector2D(-5.0d, -10.0d); break;
		case 6: //ATACANTE_DIREITO
			posicaoIni = new Vector2D(-5.0d, 10.0d); break;
		case 7: //ATACANTE_ESQUERDO
			posicaoIni = new Vector2D(-12.0d, 0.0d); break;
		default: break;
		}
		
		BTreePlayer jogador = new BTreePlayer(commander, posicaoIni);
		jogador.start();
	}

}
