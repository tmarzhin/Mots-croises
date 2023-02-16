package test;
import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prga_tp1.Grille;

public class GrilleTestJunit5 {
	private Grille g1, g2 ;
	private int hauteur, largeur ;

	@BeforeEach
	public void setUp() throws Exception
	{
		hauteur = 333 ;
		largeur = 555 ;
		g1 = new Grille(hauteur, largeur) ;
		g2 = new Grille(hauteur+1, largeur+1) ;
		System.out.println("Test de " + g1.getClass().getName()) ;
	}

	@Test
	public void testHauteurLargeur()
	{
		assertEquals (hauteur, g1.getHauteur(),
						"La hauteur est égale au 1er paramètre du constructeur") ;
		assertEquals (largeur, g1.getLargeur(),
						"La largeur est égale au 2e paramètre du constructeur") ;
	}

	@Test
	public void testCellule()
	{
		for (int l=1; l<=g1.getHauteur(); l++)
		{
			String ch1 = Integer.toString(l);
			for (int c=1; c<=g1.getLargeur(); c++)
			{
				String ch2 = ch1 + ',' + Integer.toString(c) ;
				g1.setCellule(l, c, ch2) ;
				g2.setCellule(l, c, ch2 + "x");
				assertEquals(ch2, g1.getCellule(l, c),
								"La valeur restituée doit être la valeur enregistrée") ;
			}
		}
	}

	 //@Test
	public void testPreconditions()
	{
		boolean testOK = false ;
		// Précondition de hauteur
		try
		{
			new Grille(-4, 5) ;
			testOK = false ;
		}
		catch (AssertionError e){ testOK = true ; }
		assertTrue(testOK, "Une hauteur négative doit déclencher une AssertionError") ;

		// Précondition de largeur
		try
		{
			new Grille(4, -5) ;
			testOK = false ;
		}
		catch (AssertionError e){ testOK = true ; }
		 org.junit.jupiter.api.Assertions.assertTrue(testOK, "Une largeur négative doit déclencher une AssertionError") ;

		// Précondition de 1ère coordonnée de cellule
		try
		{
			g1.setCellule(hauteur+1, 1, "hgjhgjhg");
			testOK = false ;
		}
		catch (AssertionError e){ testOK = true ; }
		assertTrue(testOK, "Tout non-respect des bornes doit causer une AssertionError") ;

		// Précondition de 2ème coordonnée de cellule
		try
		{
			g1.setCellule(1, -1, "hgjhgjhg");
			testOK = false ;
		}
		catch (AssertionError e){ testOK = true ; }
		assertTrue(testOK, "Tout non-respect des bornes doit causer une AssertionError") ;
	}
}
