# Energy-System

##### Calcan Elena-Claudia \
##### 321CA

  Programul implementeaza o simulare a unui sistem energetic.
  
  Pachete folosite:
  
    • comparators:
			‣ contine comparatori pentru sortarea producatorilor in ordinea id-urilor,
			preturilor si cantitatii pe care le ofera distribuitorilor

		• database:
			‣ contine clase despre toti consumatorii, distribuitorii, producatorii
			si toate update-urile lunare pe care acestia le au.

		• entities:
			‣ contine clase despre consumatori, distribuitori, producatori, 
			contractractele dintre consumatori si producatori si statisticile 
			lunare ale producatorilor.
			‣ pattern-ul observer este realizat ca o relatie intre producatori si 
			distribuitori in care distribuitorii sunt observatorii, iar subiectul
			este reprezentat de producatori; astfel la fiecare update distribuitorii
			isi seteaza un flag pentru schimbarea producatorilor in runda urmatoare

		• iofile:
			‣ clasa InputDataReader contine metode pentru citirea informatiilor
			despre consumatori, distribuitori si producatori din fisier JSON
			‣ clasa OutputDataWriter contine metode pentru scrierea rezultatelor
			din urma simularii in fisier JSON

		• simulation:
			‣ clasa EnergySystem implementeaza mecanismul sistemului energetic

		• strategies:
			‣ interfata EnergyChoiceStrategy este implementata de clasele:
				◦ GreenEnergyStrategy
				◦ PriceEnergyStrategy
				◦ QuantityEnergyStrategy
			‣ clasele suprascriu metoda folosita in alegerea producatorilor in
			functie de strategia distribuitorului
			‣ instanta claselor este creata folosind factory, facandu-se in functie
			de de tipul strategiei dat ca parametru, in clasa EnergyChoiceStrategyFactory
			‣ instanta clasei EnergyChoiceStrategyFactory este creata folosind Singleton

		• utils
			‣ clasa Util contine constante folosite in program

	• In Main este apelata metoda care efectueaza simularea, metoda de citire
	din fisier JSON si metoda de afisare in fisier JSON		
