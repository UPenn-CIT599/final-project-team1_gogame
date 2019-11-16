import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProblemSolutionTest {

	//@Test
	final void test1() {

		String sgfText = "(;GM[1]FF[4]SZ[19]HA[0]KM[0]GN[Cho L&D (abc)]AB[bb][cb][db][fb]AW[ea][eb][bc][cc][dc]C[Advanced](;B[ec];W[fc];B[ed];W[gb](;B[fd];W[gc](;B[ab];W[ba](;B[bd];W[cd];B[ce];W[be](;B[dd];W[ad];B[ac]C[Correct.])(;B[ac];W[ad];B[dd]C[Correct.]))(;B[ce]WV[];W[ac]C[Wrong.]))(;B[da]WV[];W[fa];B[ab];W[ba]C[Wrong.]))(;B[ab];W[ba];B[fd];W[gc](;B[bd];W[cd];B[ce];W[be](;B[dd];W[ad];B[ac]C[Correct.])(;B[ac];W[ad];B[dd]C[Correct.]))(;B[ce]WV[];W[ac]C[Wrong.]))(;B[da]WV[];W[fa];B[ab];W[ba]C[Wrong.]))(;B[da]WV[];W[fc];B[ab];W[ba]C[Wrong.]))";
		ProblemSolution ps = new ProblemSolution(sgfText);
		ps.ParseSolution();

	}

	//@Test
	final void test2() {
		String sgfText = "M[1]FF[4]SZ[19]HA[0]KM[0]GN[Cho L&D (abc)]AB[ab][bb][cb][db][eb]AW[ca][ea][fb][ac][bc][cc][dc][ec][fc]C[Elementary](;B[ba]C[Correct.])(;B[da]WV[];W[ba];B[fa];W[hb]C[Wrong.]))";
		ProblemSolution ps = new ProblemSolution(sgfText);
		ps.ParseSolution();
	}

	//@Test
	final void test3() {
		String sgfText = "(;GM[1]FF[4]SZ[19]HA[0]KM[0]GN[Cho L&D (abc)]AB[aa][ba][ca][ab][eb][cc][dc][ec][cd][be][ce]AW[da][bb][cb][db][bc][ad][bd]C[Elementary](;B[ea];W[ac](;B[ba];W[aa];B[ab]C[Correct advantageous ko.])(;B[aa]WV[];W[ba]C[Wrong.]))(;B[ae];W[ac](;B[ba];W[aa];B[ab]C[Correct advantageous ko.])(;B[aa]WV[];W[ba]C[Wrong.])))";
		ProblemSolution ps = new ProblemSolution(sgfText);
		ps.ParseSolution();
	}

	@Test
	final void test4() {
		String sgfText = "(;GM[1]FF[4]SZ[19]HA[0]KM[0]GN[Cho L&D (abc)]AB[ab][bb][cb][db][eb]AW[ca][ea][fb][ac][bc][cc][dc][ec][fc]C[Elementary](;B[ba]C[Correct.])(;B[da]WV[];W[ba];B[fa];W[hb]C[Wrong.]))";
		ProblemSolution ps = new ProblemSolution(sgfText);
		ps.ParseSolution();
	}

}
