package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sklad  {
	File sklad = new File("src\\database\\Storage.txt");
	File sklad2 = new File("src\\database\\ComponentsStorage.txt");
	private List<Integer> komponenty = new ArrayList<>();
	
	/**
	 * Vhnizdena trieda <code>Serializator</code> pre serializaciu do textoveho subora.
	 */
	Serialization serializator() {
		class Serializator implements Serialization{
			@Override
			public String serialize(HasiaciPristroj pristroj) {	
				String text = pristroj.getModelHasiacehoPristroja() + "|"  
						+ pristroj.getKategoriaHasiacehoPristroja() + "|" 
				  		+ pristroj.getTelo().getObjem() + "|" 
				  		+ pristroj.getTelo().getStav() + "|" 
				  		+ pristroj.getRokVyroby() + "|" 
				  		+ pristroj.getHadica() + "|" 
				  		+ pristroj.getRucka() + "|" 
				  		+ pristroj.getTlakomer() + "|" 
				  		+ pristroj.getKontrola();
					return text;
			}	
			@Override
			public HasiaciPristroj deserialize(String str) {
		    	  String[] items = str.split("\\|");
		    	  ModelHasiacehoPristroja m = ModelHasiacehoPristroja.valueOf(items[0]) ;
		    	  KategoriaHasiacehoPristroja k = KategoriaHasiacehoPristroja.valueOf(items[1]) ;
		    	  Objem o = Objem.valueOf(items[2]);
		    	  int i = Integer.parseInt(items[4]);
		    	  Hadica h = Hadica.valueOf(items[5]);
		          Rucka r =  Rucka.valueOf(items[6]);
		    	  Tlakomer t = Tlakomer.valueOf(items[7]);
		    	  boolean b; 
		    	  if (items[3].equals("true"))
		    		  b = true;
		    	  else 
		    		  b = false;
		          Telo telo = new Telo(o,b);
		    	  HasiaciPristroj hp = new HasiaciPristroj(m, k, i, telo, h, r, t);
		    	  if (items[8].equals("true"))
		    		  b = true;
		    	  else 
		    		  b = false;
		    	  hp.setKontrola(b);
				return hp;
			}
		}	
		return new Serializator();
	}
	
	/**
	 * Metoda vlozi hasiaci pristroj do skladu.
	 * @param pristroj hasiaci pristroj ktory bude vlozeny do skladu
	 */
	public void uloz(HasiaciPristroj pristroj) {
	      String text = serializator().serialize(pristroj);
	      try(FileWriter fw = new FileWriter(sklad, true);
	    		    BufferedWriter bw = new BufferedWriter(fw);
	    		    PrintWriter out = new PrintWriter(bw)){
	    		    out.println(text);
	    		    bw.flush();
	    		   } 
	      catch (IOException e) {
	    			System.out.println("An error occurred.");
	    		    e.printStackTrace();
	    		}
	 }
	
	/**
	 * Metoda zoberie hasiaci pristroj zo skladu.
	 * @param pristroj hasiaci pristroj ktory bude zobrati zo skladu
	 */
	 public boolean vymaz(HasiaciPristroj pristroj) {
		   boolean a = false;
		   String lineToRemove = serializator().serialize(pristroj);		    
		   try {

		      File inFile = sklad;

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        return false;
		      }

		      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		      BufferedReader br = new BufferedReader(new FileReader(sklad));
		      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

		      String line = null;

		      while ((line = br.readLine()) != null) {

		        if (!line.trim().equals(lineToRemove) || a == true) {

		          pw.println(line);
		          pw.flush();
		        }
		        else 
		        	a = true;
		      }
		      pw.close();
		      br.close();

		      //Delete the original file
		      if (!inFile.delete()) {
		        System.out.println("Could not delete file");
		        return false;
		      }

		      //Rename the new file to the filename the original file had.
		      if (!tempFile.renameTo(inFile))
		        System.out.println("Could not rename file");

		    }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		   return a;
	   }
	   
	 
		/**
		 * Metoda zisti ci sa hasiaci pristroj nachadza na sklade.
		 * @param pristroj hasiaci pristroj ktory hlada v sklade
		 */
	   public boolean najdi(HasiaciPristroj pristroj) {
		   boolean a = false;
		   String lineToFind = serializator().serialize(pristroj);
		   try {

		      File inFile = sklad;

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        return false;
		      }

		      BufferedReader br = new BufferedReader(new FileReader(sklad));
		

		      String line = null;

		      while ((line = br.readLine()) != null) {

		        if (line.trim().equals(lineToFind)) {
		        	a = true;
		        }
		      }
		      br.close();

		   }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		   return a;
	   }
	   
	   public int zistiPocet() {
		   int i = 0;
		   try {
		      File inFile = sklad;

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        return i;
		      }

		      BufferedReader br = new BufferedReader(new FileReader(sklad));
		

		      while (br.readLine() != null) 
		    	  i++;
		      br.close();

		   }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		   return i;
	   }
	   
	   
		/**
		 * Metoda zisti ci pocet komponentov na sklade.
		 */
	   
	   public void najdiKomponenty() { 
		   String file = "src\\database\\ComponentsStorage.txt";
		   try {

		      File inFile = new File(file);

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        return;
		      }

		      BufferedReader br = new BufferedReader(new FileReader(file));

		       String line = br.readLine();
		       String[] items = line.split("\\|");

		       komponenty.clear();
		       for (int i = 0; i < 10; i++ ) {
		    	   this.komponenty.add(Integer.parseInt(items[i])) ;
		    	  
		       }
		      br.close();

		   }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		  
	   }
	   
	   public String skontrolujKomponenty(String text) {
	       for (int i = 0; i < 10; i++ ) {
	    	   if (komponenty.get(i) < 1) {
	    		   if (i == 0)
	    			   text += "Musis dokupit telo s\n";
	    		   if (i == 1)
	    			   text += "Musis dokupit telo m\n";
	    		   if (i == 2)
	    			   text += "Musis dokupit telo l\n";
	    		   if (i == 3)
	    			   text += "Musis dokupit luxhose\n";
	    		   if (i == 4)
	    			   text += "Musis dokupit optihose\n";
	    		   if (i == 5)
	    			   text += "Musis dokupit kovove rucky\n";
	    		   if (i == 6)
	    			   text += "Musis dokupit plastove rucky\n";
	    		   if (i == 7)
	    			   text += "Musis dokupit M24BAR\n";
	    		   if (i == 8)
	    			   text += "Musis dokupit M32BAR\n";
	    		   if (i == 9)
	    			   text += "Musis dokupit M34BAR\n";
	    	   } 
	       }
		   return text;
	   }
	   
	   public String skontrolujKomponent(String text, int k) {
	       for (int i = 0; i < 10; i++ ) {
	    	   if (komponenty.get(i) < 1) {
	    		   if (i == 0 && k == 0)
	    			   text += "Musis dokupit telo s\n";
	    		   if (i == 1 && k == 1)
	    			   text += "Musis dokupit telo m\n";
	    		   if (i == 2 && k == 2)
	    			   text += "Musis dokupit telo l\n";
	    		   if (i == 3 && k == 3)
	    			   text += "Musis dokupit luxhose\n";
	    		   if (i == 4 && k == 4)
	    			   text += "Musis dokupit optihose\n";
	    		   if (i == 5 && k == 5)
	    			   text += "Musis dokupit kovove rucky\n";
	    		   if (i == 6 && k == 6)
	    			   text += "Musis dokupit plastove rucky\n";
	    		   if (i == 7 && k == 7)
	    			   text += "Musis dokupit M24BAR\n";
	    		   if (i == 8 && k == 8)
	    			   text += "Musis dokupit M32BAR\n";
	    		   if (i == 9 && k == 9)
	    			   text += "Musis dokupit M34BAR\n";
	    	   } 
	       }
		   return text;
	   }
	   
	   
		/**
		 * Metoda vymaze komponenty potrebne na vyrobu hasiaceho pristroja zo skladu komponentov.
		 * @param model model hasiaceho pristroja ktory sa ma vyrobit
		 */
	   public String vymazKomponenty(String model) {
		   String file = "src\\database\\ComponentsStorage.txt";
		   najdiKomponenty();
		   String text = "";
		   text = skontrolujKomponenty(text);
		   
	       if (text != "") {
	    	   text += "\n";
	    	   return text;
	    	   }
	       
	       
		   if (model == "Model1") {
			   komponenty.set(2, komponenty.get(2) - 1);
			   komponenty.set(3, komponenty.get(3) - 1);
			   komponenty.set(5, komponenty.get(5) - 1);
			   komponenty.set(7, komponenty.get(7) - 1);
		   }
		   
		   else if (model == "Model2") {
			   komponenty.set(1, komponenty.get(1) - 1);
			   komponenty.set(4, komponenty.get(4) - 1);
			   komponenty.set(6, komponenty.get(6) - 1);
			   komponenty.set(8, komponenty.get(8) - 1);
		   }
		   
		   else if (model == "Model3") {
			   komponenty.set(0, komponenty.get(0) - 1);
			   komponenty.set(3, komponenty.get(3) - 1);
			   komponenty.set(5, komponenty.get(5) - 1);
			   komponenty.set(9, komponenty.get(9) - 1);
		   }
		   else {
			   String[] items = model.split("\\|");
		
			   if(items[1].equals("S")) {
				   komponenty.set(0, komponenty.get(0) - 1);}
			   if(items[1].equals("M")) {
				   komponenty.set(1, komponenty.get(1) - 1);}
			   if(items[1].equals( "L")) {
				   komponenty.set(2, komponenty.get(2) - 1);}
			   if(items[2].equals("LUXHOSE")) {
				   komponenty.set(3, komponenty.get(3) - 1);}
			   if(items[2].equals( "OPTIHOSE")) {
				   komponenty.set(4, komponenty.get(4) - 1);}
			   if(items[3].equals("KOVOVA")) {
				   komponenty.set(5, komponenty.get(5) - 1);}
			   if(items[3].equals( "PLASTOVA")) {
				   komponenty.set(6, komponenty.get(6) - 1);}
			   if(items[4].equals( "M24BAR")) {
				   komponenty.set(7, komponenty.get(7) - 1);}
			   if(items[4].equals( "M32BAR")) {
				   komponenty.set(8, komponenty.get(8) - 1);}
			   if(items[4].equals( "M32BAR")) {
				   komponenty.set(9, komponenty.get(9) - 1);}
		   }
		   return saveKomponenty(file);
	   }

	   
	   private String saveKomponenty(String file) {
		   String line = String.valueOf(komponenty.get(0)) + "|" +
				   String.valueOf(komponenty.get(1)) + "|" +
				   String.valueOf(komponenty.get(2)) + "|" +
				   String.valueOf(komponenty.get(3)) + "|" +
				   String.valueOf(komponenty.get(4)) + "|" +
				   String.valueOf(komponenty.get(5)) + "|" +
				   String.valueOf(komponenty.get(6)) + "|" +
				   String.valueOf(komponenty.get(7)) + "|" +
				   String.valueOf(komponenty.get(8)) + "|" +
				   String.valueOf(komponenty.get(9));	    
		   try {

		      File inFile = new File(file);

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        return null;
		      }

		      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));


		      pw.println(line);
		      pw.flush();
		        		      
		      pw.close();

		      //Delete the original file
		      if (!inFile.delete()) {
		        System.out.println("Could not delete file");
		        return null;
		      }

		      //Rename the new file to the filename the original file had.
		      if (!tempFile.renameTo(inFile))
		        System.out.println("Could not rename file");

		    }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
		   return "Pristroj bol vyrobeny.\n";
	   }
	   
	   private void saveKomponenty() { 
		   String file = "src\\database\\ComponentsStorage.txt";
		   String line = String.valueOf(komponenty.get(0)) + "|" +
				   String.valueOf(komponenty.get(1)) + "|" +
				   String.valueOf(komponenty.get(2)) + "|" +
				   String.valueOf(komponenty.get(3)) + "|" +
				   String.valueOf(komponenty.get(4)) + "|" +
				   String.valueOf(komponenty.get(5)) + "|" +
				   String.valueOf(komponenty.get(6)) + "|" +
				   String.valueOf(komponenty.get(7)) + "|" +
				   String.valueOf(komponenty.get(8)) + "|" +
				   String.valueOf(komponenty.get(9));	    
		   try {

		      File inFile = new File(file);

		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		      }

		      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));


		      pw.println(line);
		      pw.flush();
		        		      
		      pw.close();

		      if (!inFile.delete()) {
		        System.out.println("Could not delete file");
		      }

		      if (!tempFile.renameTo(inFile))
		        System.out.println("Could not rename file");

		    }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		    }
	   }
	   
		public void kup(int i) {
			najdiKomponenty();
			komponenty.set(i, komponenty.get(i) + 10);
			saveKomponenty();
		}

		/**
		 * Metoda zoberie komponent zo skladu.
		 * @param i cislo komponentu
		 */
		public String pouzi(int i){
			najdiKomponenty();
			String text = "";
			text = skontrolujKomponent(text,i);
			if(text.equals("")) {
				komponenty.set(i, komponenty.get(i) - 1);
				saveKomponenty();
	    		   if (i == 0)
	    			   return "Komponent telo s bol odobraty zo skladu.\n";
	    		   else if (i == 1)
	    			   return "Komponent telo m bol odobraty zo skladu.\n";
	    		   else if (i == 2)
	    			   return "Komponent telo l bol odobraty zo skladu.\n";
	    		   else if (i == 3)
	    			   return "Komponent luxhose bol odobraty zo skladu.\n";
	    		   else if (i == 4)
	    			   return "Komponent optihose bol odobraty zo skladu.\n";
	    		   else if (i == 5)
	    			   return "Komponent kovova rucka bol odobraty zo skladu.\n";
	    		   else if (i == 6)
	    			   return "Komponent plastova rucka bol odobraty zo skladu.\n";
	    		   else if (i == 7)
	    			   return "Komponent M24BAR bol odobraty zo skladu.\n";
	    		   else if (i == 8)
	    			   return "Komponent M32BAR bol odobraty zo skladu.\n";
	    		   else  if (i == 9 )
	    			   return "Komponent M34BAR bol odobraty zo skladu.\n";
	    		   else 
	    			   return " Chyba.\n";
	    		   }
			
			
			else
				return text;
		}
		
		/**
		 * Metoda posle prvy hasiaci pristroj zo skladu opravarovi.
		 */
		public HasiaciPristroj posliDoOpravy() {
			HasiaciPristroj hp = null;
			
			try {
			      File inFile = sklad;

			      if (!inFile.isFile()) {
			        System.out.println("Parameter is not an existing file");
			      }

			      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			      BufferedReader br = new BufferedReader(new FileReader(sklad));
			      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			      String line = null;
			      
			      if ((line = br.readLine()) != null) {
			    	  hp = serializator().deserialize(line);
			      }
			    	  
			      while ((line = br.readLine()) != null) {

			      pw.println(line);
			      pw.flush();
			  

			      }
			      pw.close();
			      br.close();

			      if (!inFile.delete()) {
			        System.out.println("Could not delete file");
			      }

			      if (!tempFile.renameTo(inFile))
			        System.out.println("Could not rename file");

			    }
			    catch (FileNotFoundException ex) {
			      ex.printStackTrace();
			    }
			    catch (IOException ex) {
			      ex.printStackTrace();
			    }
			return hp;
		}	
		
}