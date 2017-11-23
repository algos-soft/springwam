BootStrap

La classe DefaultListableBeanFactory nel metodo preInstantiateSingletons,
spazzola tutti i @SpringComponent che trova nella List<String> beanDefinitionNames

	/** List of bean definition names, in registration order */
	private volatile List<String> beanDefinitionNames = new ArrayList<String>(256);

1) Ogni classi di 'beanDefinitionNames'
2) Costruttore della classe
3) Classi 'injected' nel suddetto costruttore
4) A cascata, si risale a tutte le classi a loro volta 'injected' nei costruttori
5) Vengono costruite tutte le classi contenute in 'beanDefinitionNames' e tutte quelle iniettate nei costruttori
6) Per ogni classe, viene eseguito un (eventuale) metodo annotato @PostConstruct (qualsiasi firma)

After the Spring context has been initialized

7) Viene eseguito, in tutte le classi che ce l'hanno, il metodo
    onApplicationEvent(ContextRefreshedEvent event), annotato @EventListener
8) L'utilizzo nelle classi di BootStrap dell'interfaccia 'implements InitializingBean',
    è sostituito dal più recente uso del punto 7)
9) Il metodo del punto 7) viene eseguito in un ordine INCONTROLLABILE ma dopo aver costruito TUTTI i beans previsti
10) I nomi del package e delle singole classi sono ininfluenti. Uso il suffisso xxxBoot solo per 'estetica'