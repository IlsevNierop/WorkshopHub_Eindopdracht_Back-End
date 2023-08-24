-- all users have the same password: password
-- Melania (ID 100) is admin

INSERT INTO users (id, first_name, last_name, email, password, company_name, kvk_number, vat_number,
                   workshop_owner_verified, workshop_owner, file_name, profile_pic_url)
VALUES (100, 'Melania', 'Rossi', 'melania@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', 'Admin Bedrijf', 12345678, 'NL123456789B01',
        true, true, 'pexels-melania-piacquadio-774909.webp', 'http://localhost:8080/downloadprofilepic/pexels-melania-piacquadio-774909.webp'),
       (104, 'Lukas', 'Muller', 'lukas@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', 'Admin Bedrijf2', 12345679, 'NL123456791B01',
        true, true, 'pexels-lukas-pixabay-220453.webp', 'http://localhost:8080/downloadprofilepic/pexels-lukas-pixabay-220453.webp'),
       (105, 'Andrea', 'Davis', 'andrea@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Voorbeeld Bedrijf', 12345678, 'NL123456789B01', true, true, 'Profielfoto-Andrea.webp',
        'http://localhost:8080/downloadprofilepic/Profielfoto-Andrea.webp'),
       (106, 'Stefan', 'Jansen', 'stefan@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Test Bedrijf', 98765432, 'NL987654321B01', true, true, 'pexels-stefan-stefancik-91227.webp',
        'http://localhost:8080/downloadprofilepic/pexels-stefan-stefancik-91227.webp'),
       (107, 'Dziana', 'Davis', 'dziana@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Voorbeeld BV', 54321678, 'NL543216789B01', true, true, 'pexels-dziana-hasanbekava-7275385.webp',
        'http://localhost:8080/downloadprofilepic/pexels-dziana-hasanbekava-7275385.webp'),
       (108, 'Emily', 'Brown', 'emily@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Test BV', 87654321, 'NL876543219B01', default, true, 'pexels-monstera-5876695.webp',
        'http://localhost:8080/downloadprofilepic/pexels-monstera-5876695.webp');


INSERT INTO users (id, first_name, last_name, email, password, workshop_owner, file_name, profile_pic_url)
VALUES (101, 'Samuel', 'Smith', 'samuel.smith@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        false, 'pexels-laker-5792641.webp', 'http://localhost:8080/downloadprofilepic/pexels-laker-5792641.webp'),
       (102, 'Isabella', 'Janssen', 'isabella.janssen@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', false,
        'pexels-vinicius-wiesehofer-1130626.webp', 'http://localhost:8080/downloadprofilepic/pexels-vinicius-wiesehofer-1130626.webp'),
       (103, 'Kees', 'Janssen', 'kees@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', false, null, null);

INSERT INTO authorities (user_id, authority)
values (100, 'ROLE_CUSTOMER'),
       (100, 'ROLE_ADMIN'),
       (101, 'ROLE_CUSTOMER'),
       (102, 'ROLE_CUSTOMER'),
       (103, 'ROLE_CUSTOMER'),
       (104, 'ROLE_CUSTOMER'),
       (104, 'ROLE_WORKSHOPOWNER'),
       (104, 'ROLE_ADMIN'),
       (105, 'ROLE_WORKSHOPOWNER'),
       (105, 'ROLE_CUSTOMER'),
       (106, 'ROLE_CUSTOMER'),
       (106, 'ROLE_WORKSHOPOWNER'),
       (107, 'ROLE_CUSTOMER'),
       (107, 'ROLE_WORKSHOPOWNER'),
       (108, 'ROLE_CUSTOMER');


INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (100, 'Indonesische kook workshop', '2023-10-01', '16:30:00', '20:00:00', 99.0, 'INDOORS', 'Utrecht',
        'Inclusief eten en drinken. Allergien wordt rekening mee gehouden.', 'Ontdek de exotische smaken van Indonesië tijdens onze meeslepende kookworkshop. Stap binnen in een wereld van kruiden, specerijen en traditionele recepten terwijl we je meenemen op een culinaire reis door de Indonesische keuken.
Onze ervaren chef-kok begroet je met een warm welkom en introduceert je in de geheimen van de Indonesische keuken. Je leert authentieke gerechten bereiden, zoals nasi goreng, saté, rendang en gado-gado, waarbij je gebruikmaakt van verse ingrediënten en traditionele kooktechnieken.
Tijdens de workshop krijg je de kans om zelf actief de handen uit de mouwen te steken en te experimenteren met de smaken en texturen van de Indonesische keuken. De chef-kok begeleidt je stap voor stap en deelt zijn/haar kennis over de verschillende kruiden en specerijen die worden gebruikt in de Indonesische gerechten.
Na afloop van de kookworkshop geniet je samen met de andere deelnemers van een heerlijke maaltijd die je zelf hebt bereid. Het is een geweldige gelegenheid om nieuwe mensen te ontmoeten, ervaringen uit te wisselen en te genieten van de verrukkelijke Indonesische gerechten die je hebt gemaakt.
Kom en ontdek de magie van de Indonesische keuken tijdens onze boeiende kookworkshop. Laat je smaakpapillen verwennen en ga naar huis met nieuwe culinaire vaardigheden en een diepgaande waardering voor de rijke eetcultuur van Indonesië.',
        10, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', true, 105, 'Koken.webp',
        'http://localhost:8080/downloadworkshoppic/Koken.webp'),
       (101, 'Kaarsen maken', '2023-10-03', '13:00:00', '16:00:00', 45.0, 'INDOORS', 'Leiden',
        'Inclusief iets lekkers te eten en drinken. Je neemt 5 kaarsen mee naar huis.', 'Stap in de betoverende wereld van kaarsen maken tijdens onze inspirerende workshop. Laat je creativiteit de vrije loop en ontdek de kunst van het maken van unieke en prachtig geurende kaarsen.
Bij aanvang van de workshop verwelkomen we je in een sfeervolle omgeving, waar de heerlijke geur van verschillende wassen en oliën je tegemoet komt. Onze deskundige kaarsenmaker leidt je door het proces en deelt zijn/haar kennis en tips over het maken van hoogwaardige kaarsen.
Je leert verschillende technieken, zoals gieten, dompelen en vormen, om een verscheidenheid aan kaarsen te creëren. Ontwerp je eigen kleurenpalet, experimenteer met geuren en kies uit een scala aan decoratieve elementen om je kaarsen een persoonlijk tintje te geven.
Tijdens de workshop kun je ook meer te weten komen over de verschillende soorten was en lonten die gebruikt worden in het kaarsen maken. Je krijgt inzicht in de veiligheidsrichtlijnen en de juiste manier om kaarsen te branden en te onderhouden.
Aan het einde van de workshop ga je naar huis met een set prachtige handgemaakte kaarsen, die je kunt gebruiken om je huis te verfraaien of als een uniek cadeau voor vrienden en familie.
Kom en laat je creativiteit opvlammen tijdens onze boeiende workshop Kaarsen maken. Ontdek de magie van het creëren van je eigen kaarsen en geniet van de ontspannende en geurige ambiance die kaarsen brengen.',
        14, 'Kaarsen', null, true, 'ziet er goed uit, mag online', true, 106, 'Kaarsen1.webp',
        'http://localhost:8080/downloadworkshoppic/Kaarsen1.webp'),
       (102, 'Patisserie workshop', '2023-06-15', '11:00:00', '14:00:00', 55.0, 'INDOORS', 'Utrecht',
        'Je krijg je eigengemaakte gebak mee naar huis.', 'Welkom bij onze exclusieve 3 uur durende workshop Patisserie in het hart van Utrecht. Deze intieme workshop biedt een unieke gelegenheid voor maximaal 6 deelnemers om onder begeleiding van een ervaren patissier de geheimen van de patisserie te ontdekken.
De workshop vindt plaats in een sfeervolle en goed uitgeruste patisseriekeuken, waar je wordt verwelkomd met een warme glimlach en een kopje koffie of thee. Onze professionele patissier zal je begeleiden bij elke stap van het proces en je voorzien van waardevolle tips en technieken om succesvolle patisseriecreaties te maken.
Gedurende de 3 uur durende workshop leer je een verscheidenheid aan patisserievaardigheden, zoals het maken van perfect krokant deeg, het bereiden van smaakvolle vullingen en het decoreren van je creaties als een ware professional. Je gaat aan de slag met klassieke recepten en leert ook enkele moderne twisten aan te brengen.
De workshop is interactief en hands-on, waarbij elke deelnemer de kans krijgt om actief mee te doen en zijn/haar eigen gebak te creëren. Aan het einde van de workshop is er een gezamenlijke proeverij, waarbij je kunt genieten van de heerlijke resultaten van je inspanningen.
De locatie van de workshop bevindt zich in het bruisende centrum van Utrecht, dicht bij het openbaar vervoer en andere bezienswaardigheden. Het is de perfecte gelegenheid om een culinaire ervaring te combineren met het verkennen van de prachtige stad.
Kortom, deze 3 uur durende Patisserie workshop in Utrecht is een unieke gelegenheid om je vaardigheden op het gebied van patisserie te verbeteren, nieuwe vrienden te maken en te genieten van een smakelijke en gezellige ervaring. Reserveer nu en laat je betoveren door de zoete wereld van de patisserie.',
        9, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', true, 105, 'Bakken--1.webp',
        'http://localhost:8080/downloadworkshoppic/Bakken--1.webp'),
       (103, 'Macramé plantenhanger maken', '2023-10-01', '14:30:00', '16:30:00', 15.0, 'INDOORS', 'Amsterdam',
        'Er is thee en koffie aanwezig.', 'Kom en ontdek de kunst van macramé tijdens onze creatieve 2 uur durende workshop in Amsterdam. Leer hoe je prachtige plantenhangers kunt maken met de oude techniek van knopen, en geef je huis een stijlvolle en groene touch.
De workshop wordt gehouden in een inspirerende locatie in het hart van Amsterdam. Bij aankomst word je verwelkomd met een warme sfeer en alle benodigde materialen voor het maken van je eigen macramé plantenhanger. Onze ervaren instructeur leert je stap voor stap de basistechnieken en de verschillende knopen die je nodig hebt om een prachtig ontwerp te creëren.
Met een kleine groep van maximaal 12 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginner bent of al ervaring hebt met macramé, deze workshop is geschikt voor alle niveaus. Je kunt je eigen creativiteit de vrije loop laten en je plantenhanger personaliseren met verschillende knooppatronen en versieringen.
De kosten voor de workshop bedragen slechts 15 euro per persoon, inclusief alle materialen die nodig zijn om je eigen macramé plantenhanger te maken. Daarnaast krijg je waardevolle tips en tricks van de instructeur, zodat je ook thuis verder kunt gaan met je nieuwe vaardigheden.
Laat je inspireren door de trendy wereld van macramé en ga naar huis met een prachtige handgemaakte plantenhanger om je huis mee op te fleuren. Schrijf je nu in voor deze 2 uur durende workshop in Amsterdam en geniet van een creatieve en ontspannende ervaring samen met gelijkgestemde deelnemers.
', 12, 'Handwerk', 'Breien', true, default, true, 106, 'Macrame.webp', 'http://localhost:8080/downloadworkshoppic/Macrame.webp'),
       (104, 'Workshop breien voor beginners', '2023-10-08', '10:00:00', '13:00:00', 30.0, 'INDOORS', 'Amsterdam',
        'Ook gevorderden zijn welkom.', 'Ontdek de vreugde van breien tijdens onze boeiende 3 uur durende workshop voor beginners in Amsterdam. Of je nu nog nooit een haaknaald hebt vastgehouden of een beetje basiskennis hebt, deze workshop is ontworpen om je de essentiële vaardigheden te leren om prachtige handgemaakte creaties te maken.
De workshop vindt plaats in een gezellige en inspirerende omgeving in het hart van Amsterdam. Bij aankomst ontvang je een warm welkom en alle benodigde materialen om aan de slag te gaan. Onze ervaren instructeur begeleidt je stap voor stap door de basissteken en technieken, en leert je hoe je eenvoudige projecten zoals sjaals, mutsen of amigurumi kunt maken.
Met een kleine groep van maximaal 10 deelnemers is er volop gelegenheid voor persoonlijke begeleiding en interactie. Je krijgt de kans om vragen te stellen, tips te krijgen en te leren van de ervaring van de instructeur. Bovendien is het een geweldige gelegenheid om nieuwe mensen te ontmoeten die dezelfde passie delen.
De kosten voor de workshop bedragen slechts 30 euro per persoon, inclusief alle materialen die je nodig hebt om te breien. Naast de praktische vaardigheden ontvang je ook een handige handleiding met instructies en patronen, zodat je thuis verder kunt oefenen.
Laat je creativiteit de vrije loop en ontdek de ontspannende wereld van breien tijdens deze 3 uur durende workshop. Schrijf je nu in en geniet van een gezellige en leerzame ervaring in Amsterdam. Of je nu een uniek cadeau wilt maken of gewoon wilt ontspannen met een creatieve bezigheid, breien biedt eindeloze mogelijkheden.',
        10, 'Handwerk', 'Breien', true, 'Ziet er interessant uit', true, 106, 'Breien8.webp',
        'http://localhost:8080/downloadworkshoppic/Breien8.webp'),
       (105, 'Workshop keramiek: draaien', '2023-11-01', '14:00:00', '17:00:00', 65.0, 'INDOORS', 'Leiden', default, 'Stap binnen in de fascinerende wereld van keramiek tijdens onze inspirerende 3 uur durende workshop: Draaien. Deze exclusieve workshop, gehouden in Haarlem, biedt een intieme setting voor maximaal 6 deelnemers om de kunst van het keramiekdraaien te verkennen.
De workshop vindt plaats in een prachtig atelier, waar je wordt verwelkomd met een warme sfeer en een deskundige keramist als je gids. Je leert de basisprincipes van het draaien op een draaischijf en krijgt hands-on begeleiding om je vaardigheden te ontwikkelen en te verfijnen.
Met een kleine groep van maximaal 6 deelnemers is er voldoende ruimte voor persoonlijke aandacht en begeleiding. De ervaren keramist zal je stap voor stap door het proces leiden, je helpen bij het centreren van de klei, het vormgeven van je creaties en het creëren van prachtige keramische stukken.
De workshop biedt een unieke gelegenheid om je eigen keramiek te creëren, zoals kommen, mokken, vazen en meer. Je kunt experimenteren met verschillende vormen, texturen en decoratieve technieken om een persoonlijk en uniek stuk te maken.
De kosten voor de workshop bedragen 65 euro per persoon, inclusief alle materialen en het bakken van je keramische creaties. Aan het einde van de workshop worden je werken gebakken en kun je ze op een later tijdstip ophalen om ze te glazuren en af te werken.
Laat je creativiteit de vrije loop en geniet van een unieke ervaring met de workshop Keramiek: Draaien. Reserveer nu je plek in deze exclusieve workshop en laat je inspireren door de magie van het keramiekdraaien.',
        8, 'Keramiek', null, true, 'ziet er goed uit, mag online', true, 106, 'Keramiek1.webp',
        'http://localhost:8080/downloadworkshoppic/Keramiek1.webp'),
       (106, 'Eetbare paddenstoelen', '2023-11-13', '15:00:00', '16:30:00', 25.0, 'OUTDOORS', 'Woerden',
        'Inclusief een klein paddenstoelen hapje', 'Welkom bij onze boeiende 1,5 uur durende workshop Eetbare Paddenstoelen in de prachtige buitenomgeving van Woerden. Tijdens deze workshop leer je alles over de fascinerende wereld van eetbare paddenstoelen en ontdek je hoe je ze kunt identificeren en gebruiken in je eigen culinaire creaties.
De workshop vindt plaats in een sfeervolle buitenlocatie in Woerden, waar je wordt omringd door de natuurlijke pracht van het landschap. Onze deskundige gids, een ervaren paddenstoelenkenner, neemt je mee op een informatieve en interactieve wandeling door het bos, waar je leert hoe je eetbare paddenstoelen kunt herkennen, plukken en gebruiken.
Met een kleine groep van maximaal 8 deelnemers is er voldoende ruimte voor persoonlijke aandacht en interactie met de gids. Je krijgt waardevolle tips en technieken om veilig en verantwoord eetbare paddenstoelen te verzamelen, en je leert over de verschillende soorten paddenstoelen en hun culinaire toepassingen.
De kosten voor de workshop bedragen slechts 25 euro per persoon, inclusief begeleiding door de paddenstoelenexpert en informatiemateriaal om mee naar huis te nemen. Het is een geweldige kans om je kennis over eetbare paddenstoelen uit te breiden en een nieuwe dimensie toe te voegen aan je culinaire avonturen.
Kom en sluit je aan bij onze inspirerende workshop Eetbare Paddenstoelen in Woerden. Leer over de wonderen van de natuur, ontdek nieuwe smaken en laat je inspireren door de mogelijkheden van eetbare paddenstoelen. Reserveer nu je plek en maak deel uit van deze unieke en smakelijke ervaring.',
        8, 'Wildplukken', 'Natuur', default, default, default, 105, 'Wildplukken2.webp',
        'http://localhost:8080/downloadworkshoppic/Wildplukken2.webp'),
       (107, 'Creatief schilderen', '2023-05-01', '18:30:00', '21:30:00', 35.0, 'INDOORS', 'Utrecht',
        'Inclusief eten en drinken. Inclusief materialen.', 'Welkom bij onze inspirerende 3 uur durende workshop Creatief Schilderen in het bruisende Utrecht. Deze workshop biedt een uitgelezen kans voor maximaal 8 deelnemers om hun creativiteit te uiten en nieuwe schildervaardigheden te ontdekken.
De workshop vindt plaats in een sfeervol atelier in Utrecht, waar je wordt verwelkomd met een warme ambiance en alle benodigde materialen om aan de slag te gaan. Onze ervaren kunstenaar begeleidt je stap voor stap door het creatieve proces, waarbij je de vrijheid hebt om je eigen stijl en interpretatie te ontwikkelen.
Met een kleine groep van maximaal 8 deelnemers is er volop ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginnende kunstenaar bent of al wat ervaring hebt, deze workshop is geschikt voor alle niveaus. Je leert verschillende technieken, kleurenpaletten en compositieprincipes om expressieve en unieke schilderijen te creëren.
De kosten voor de workshop bedragen slechts 35 euro per persoon, inclusief alle schildermaterialen en een canvas om je kunstwerk op te maken. Je krijgt ook waardevolle feedback en begeleiding van de kunstenaar om je te helpen je creatieve vaardigheden verder te ontwikkelen.
Laat je verbeelding de vrije loop en geniet van een ontspannende en inspirerende schilderervaring tijdens deze 3 uur durende workshop. Schrijf je nu in en maak deel uit van een creatieve gemeenschap in Utrecht, waar je kunt ontdekken, leren en je eigen kunstwerken kunt creëren.',
        8, 'Schilderen', null, true, 'ziet er goed uit, mag online', default, 106, 'Schilderen3.webp',
        'http://localhost:8080/downloadworkshoppic/Schilderen3.webp'),
       (108, 'Taarten bakken', '2023-09-05', '10:00:00', '13:00:00', 42.50, 'INDOORS', 'Amsterdam', default, 'Stap in de heerlijke wereld van taarten bakken tijdens onze smakelijke 3 uur durende workshop in Amsterdam. Deze workshop biedt een unieke gelegenheid voor maximaal 8 deelnemers om te leren hoe ze prachtige en verrukkelijke taarten kunnen maken.
De workshop wordt gehouden in een gezellige bakkerij in Amsterdam, waar je wordt begroet met de heerlijke geur van versgebakken lekkernijen. Onze ervaren banketbakker zal je begeleiden bij elke stap van het proces, van het bereiden van het deeg tot het maken van de perfecte vullingen en decoraties.
Met een kleine groep van maximaal 8 deelnemers is er voldoende ruimte voor persoonlijke aandacht en interactie. Je leert verschillende technieken en krijgt waardevolle tips en trucs om je taarten naar een hoger niveau te tillen. Of je nu een beginner bent of al wat ervaring hebt, deze workshop is geschikt voor alle niveaus.
De kosten voor de workshop bedragen 42,50 euro per persoon, inclusief alle ingrediënten en materialen die nodig zijn om je eigen taarten te maken. Je gaat naar huis met nieuwe vaardigheden, een schat aan kennis en natuurlijk je zelfgemaakte taarten om te delen en van te genieten.
Laat je creativiteit en smaakpapillen de vrije loop tijdens deze 3 uur durende workshop Taarten Bakken. Schrijf je nu in en ontdek de geheimen van het maken van heerlijke taarten in een gezellige en leerzame omgeving.',
        8, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', default, 105, 'Bakken-.webp',
        'http://localhost:8080/downloadworkshoppic/Bakken-.webp'),
       (109, 'Brood bakken', '2023-10-15', '10:00:00', '12:00:00', 20.0, 'INDOORS', 'Woerden',
        'Alle ingrediënten zijn inbegrepen.', 'Ontdek de kunst van het brood bakken tijdens onze interactieve workshop in Woerden. Leer hoe je heerlijk vers brood kunt maken van scratch en geniet van de heerlijke geur van versgebakken brood in je eigen keuken.
De workshop wordt gehouden in een sfeervolle locatie in het hart van Woerden. Bij aankomst word je verwelkomd met een warme sfeer en alle benodigde ingrediënten en gereedschappen om je eigen brood te bakken. Onze ervaren bakker begeleidt je stap voor stap door het proces, van het mengen van de ingrediënten tot het kneden van het deeg en het bakken van het brood.
Met een kleine groep van maximaal 10 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginnende bakker bent of al ervaring hebt, deze workshop is geschikt voor alle niveaus. Je leert verschillende technieken en krijgt handige tips en tricks om ervoor te zorgen dat je thuis ook succesvol brood kunt bakken.
Tijdens de workshop maak je verschillende soorten brood, zoals een klassiek wit brood, een gezond volkorenbrood en een heerlijk knapperig stokbrood. Je leert ook hoe je creatieve vormen kunt geven aan je brood en hoe je verschillende smaken en texturen kunt bereiken.
De kosten voor de workshop bedragen 20 euro per persoon, inclusief alle ingrediënten en gereedschappen die nodig zijn om je eigen brood te bakken. Daarnaast ontvang je een receptenboekje met de besproken technieken en recepten, zodat je ook thuis verder kunt oefenen en experimenteren.
Laat je inspireren door de geheimen van het broodbakken en ga naar huis met versgebakken brood en nieuwe vaardigheden. Schrijf je nu in voor deze 2 uur durende workshop in Woerden en geniet van een smakelijke en leerzame ervaring samen met andere broodliefhebbers.
', 10, 'Koken', 'Bakken', true, default, true, 105, 'Brood-bak-foto3.webp',
        'http://localhost:8080/downloadworkshoppic/Brood-bak-foto3.webp'),
       (110, 'Jam maken', '2023-11-05', '13:00:00', '15:00:00', 25.0, 'INDOORS', 'Den Haag',
        'Alle ingrediënten en potten zijn inbegrepen.', 'Ontdek de zoete en fruitige wereld van jam maken tijdens onze hands-on workshop in Den Haag. Leer hoe je je eigen heerlijke jam kunt maken met seizoensfruit en geniet van de smaak van zelfgemaakte lekkernijen.
De workshop wordt gehouden in een gezellige locatie in het hart van Den Haag. Bij aankomst word je verwelkomd met een warme sfeer en alle benodigde ingrediënten, keukengerei en potten om je eigen jam te maken. Onze ervaren jammaker begeleidt je stap voor stap door het proces, van het selecteren van de beste vruchten tot het inmaken van de jam.
Met een kleine groep van maximaal 8 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginner bent of al ervaring hebt met het maken van jam, deze workshop is geschikt voor alle niveaus. Je leert verschillende technieken en krijgt handige tips en tricks om ervoor te zorgen dat je thuis ook heerlijke jam kunt maken.
Tijdens de workshop maak je verschillende smaken jam, afhankelijk van het seizoensfruit dat beschikbaar is. Je leert hoe je de vruchten kunt bereiden, de juiste verhoudingen van suiker en pectine kunt gebruiken en hoe je de jam kunt inmaken voor langdurige houdbaarheid. Je kunt ook experimenteren met toevoegingen zoals kruiden, specerijen of zelfs een vleugje likeur om je jam een unieke twist te geven.
De kosten voor de workshop bedragen 25 euro per persoon, inclusief alle ingrediënten, keukengerei en potten die nodig zijn om je eigen jam te maken. Daarnaast ontvang je een receptenboekje met de besproken technieken en recepten, zodat je ook thuis verder kunt experimenteren en genieten van zelfgemaakte jam.
Laat je smaakpapillen prikkelen door de kunst van het jam maken en ga naar huis met heerlijke potjes jam en nieuwe vaardigheden. Schrijf je nu in voor deze 2 uur durende workshop in Den Haag en geniet van een zoete en leerzame ervaring samen met andere jamliefhebbers.
', 8, 'Koken', 'Conserven maken', true, default, true, 107, 'Jam3.webp',
        'http://localhost:8080/downloadworkshoppic/Jam3.webp'),
       (111, 'Kaarsen maken', '2023-12-10', '14:00:00', '16:00:00', 30.0, 'INDOORS', 'Haarlem',
        'Alle materialen zijn inbegrepen.', 'Kom en ontdek de ambachtelijke wereld van het kaarsen maken tijdens onze creatieve workshop in Haarlem. Leer hoe je je eigen prachtige en geurige kaarsen kunt maken en creëer een gezellige sfeer in je huis.
De workshop wordt gehouden in een inspirerende locatie in het hart van Haarlem. Bij aankomst word je verwelkomd met een warme sfeer en alle benodigde materialen, zoals sojawas, lonten, geuroliën en kleurstoffen, om je eigen kaarsen te maken. Onze ervaren kaarsenmaker begeleidt je stap voor stap door het proces, van het smelten van de was tot het gieten van de kaarsen en het toevoegen van geur en kleur.
Met een kleine groep van maximaal 10 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginner bent of al ervaring hebt met het maken van kaarsen, deze workshop is geschikt voor alle niveaus. Je leert verschillende technieken en krijgt handige tips en tricks om ervoor te zorgen dat je thuis ook prachtige kaarsen kunt maken.
Tijdens de workshop maak je verschillende soorten kaarsen, zoals geurkaarsen, kleurrijke dinerkaarsen en decoratieve kaarsen. Je leert hoe je verschillende vormen en patronen kunt creëren en hoe je de juiste geurintensiteit kunt bereiken. Je kunt ook experimenteren met het mengen van geuroliën om je eigen unieke geurcombinaties te creëren.
De kosten voor de workshop bedragen 30 euro per persoon, inclusief alle materialen die nodig zijn om je eigen kaarsen te maken. Daarnaast ontvang je een handleiding met de besproken technieken en recepten, zodat je ook thuis verder kunt experimenteren en genieten van je zelfgemaakte kaarsen.
Laat je creativiteit branden en ga naar huis met prachtige handgemaakte kaarsen en nieuwe vaardigheden. Schrijf je nu in voor deze 2 uur durende workshop in Haarlem en geniet van een gezellige en leerzame ervaring samen met andere kaarsenliefhebbers.
', 10, 'Creatief', 'Decoratie', true, default, true, 107, 'Kaarsen7.webp',
        'http://localhost:8080/downloadworkshoppic/Kaarsen7.webp'),
       (112, 'Naaiworkshop', '2023-12-15', '10:00:00', '13:00:00', 40.0, 'INDOORS', 'Amsterdam',
        'Naaimachines zijn aanwezig.', 'Ontdek de kunst van het naaien tijdens onze hands-on naaiworkshop in Amsterdam. Leer hoe je je eigen kledingstukken kunt maken, herstellen en aanpassen en ontwikkel je vaardigheden als naaister.
De workshop wordt gehouden in een inspirerende locatie in het hart van Amsterdam. Bij aankomst word je verwelkomd met een warme sfeer en staan er naaimachines voor je klaar. Of je nu een beginner bent of al enige ervaring hebt met naaien, onze ervaren naaister begeleidt je stap voor stap door het proces.
Met een kleine groep van maximaal 6 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Tijdens de workshop leer je verschillende naaitechnieken, zoals het nemen van maten, het knippen van stoffen, het inzetten van ritsen en het maken van zomen. Je kunt kiezen uit een aantal eenvoudige projecten, zoals een rok, een tas of een kussenhoes, afhankelijk van je vaardigheidsniveau en interesses.
De kosten voor de workshop bedragen 40 euro per persoon, inclusief het gebruik van naaimachines en basisnaaigereedschap. Je dient zelf stoffen en eventuele extra materialen voor je project mee te nemen. Onze naaister staat klaar om je te begeleiden, tips te geven en al je vragen te beantwoorden.
Laat je creativiteit los en ga naar huis met een uniek kledingstuk of accessoire dat je zelf hebt gemaakt. Schrijf je nu in voor deze 3 uur durende naaiworkshop in Amsterdam en ontwikkel je vaardigheden als naaister in een gezellige en leerzame omgeving.
', 6, 'Creatief', 'Handwerk', true, default, true, 106, 'Naaien5.webp',
        'http://localhost:8080/downloadworkshoppic/Naaien5.webp'),
       (113, 'Wildplukworkshop', '2024-01-20', '11:00:00', '14:00:00', 35.0, 'OUTDOORS', 'Amsterdam',
        'Geschikt voor alle leeftijden.', 'Doe mee aan onze boeiende wildplukworkshop in Amsterdam en ontdek de eetbare schatten die de natuur te bieden heeft. Leer hoe je veilig en verantwoord wilde planten kunt identificeren, plukken en gebruiken in je keuken.
De workshop vindt plaats in de prachtige natuurlijke omgeving rondom Amsterdam. Samen met onze ervaren gids ga je op ontdekkingsreis door bossen, velden en oevers, waar je leert hoe je eetbare planten kunt herkennen, zoals wilde kruiden, bessen, noten en paddenstoelen. Je leert ook over de geneeskrachtige eigenschappen van bepaalde planten.
Met een kleine groep van maximaal 15 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Onze gids deelt zijn kennis en ervaring en geeft handige tips voor het plukken en gebruiken van wilde planten. Je leert over de seizoensgebondenheid, de juiste manieren van plukken en hoe je de geoogste ingrediënten kunt verwerken in heerlijke gerechten.
Tijdens de workshop proef je ook enkele bereide wildpluklekkernijen en krijg je receptideeën om thuis verder te experimenteren. De nadruk ligt op duurzaamheid en respect voor de natuur, waarbij we alleen wildplukken wat de omgeving kan missen en rekening houden met de biodiversiteit.
De kosten voor de workshop bedragen 35 euro per persoon, inclusief begeleiding, informatiebrochure, proeverij en enkele wildplukproducten. De workshop is geschikt voor alle leeftijden, dus breng gerust je kinderen mee om ze te laten kennismaken met de wonderen van de natuur.
Laat je verwonderen door de overvloed van de natuur en ga naar huis met nieuwe kennis en inspiratie om wilde planten in je eigen keuken te gebruiken. Schrijf je nu in voor deze 3 uur durende wildplukworkshop in Amsterdam en beleef een unieke en educatieve ervaring in de natuur.
', 15, 'Natuur', 'Koken', true, default, true, 107, 'Wildplukken4.webp',
        'http://localhost:8080/downloadworkshoppic/Wildplukken4.webp');

INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (101, '2023-06-01', 'Geweldige workshop!', 2, 101, 102, 90.0),
       (102, '2023-05-29', 'Ik kijk ernaar uit! En ben vegetarier.', 1, 101, 102, 45.0),
       (103, '2023-05-30', 'Leuk concept!', 3, 103, 100, 45.0),
       (104, '2023-06-01', 'Ik breng twee vrienden mee!', 3, 102, 101, 105.0),
       (105, '2023-05-31', 'Interessante workshop! Wij moeten een half uurtje eerder weg', 2, 104, 100, 50.0),
       (106, '2023-05-31', default, 3, 102, 102, 50.0),
       (107, '2023-05-31', default, 3, 102, 103, 50.0),
       (108, '2023-03-31', 'Interessante workshop! Wij moeten een half uurtje eerder weg', 2, 107, 100, 30.0);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (100, 4.5,
        'De workshop was echt geweldig! Ik heb zoveel geleerd en de docent was zeer behulpzaam. Een aanrader!', default,
        default, 102, 101),
       (101, 3.2,
        'De workshop was redelijk, maar ik had verwacht dat het interactiever zou zijn. De inhoud was wel interessant.',
        true, 'Dank je wel voor je feedback. We zullen je opmerkingen in overweging nemen.', 102, 102),
       (102, 5.0,
        'Ik ben zeer tevreden met de workshop! Het was inspirerend en de sfeer was geweldig. Ik raad het iedereen aan!',
        true, 'We zijn blij dat je ervan genoten hebt. Bedankt voor je positieve woorden!', 101, 101),
       (103, 2.3,
        'Helaas voldeed de workshop niet aan mijn verwachtingen. De inhoud was te oppervlakkig en ik vond de organisatie slecht.',
        true, 'Review mag online', 102,
        107),
       (104, 4.7,
        'De workshop was fantastisch! Ik heb zoveel geleerd en het heeft mijn creatieve vaardigheden echt verbeterd. Een geweldige ervaring!',
        default, default, 102, 100),
       (105, 4.3,
        'De workshop was informatief en boeiend. Ik heb genoten van de praktische activiteiten en de instructeur had veel kennis. Over het algemeen een geweldige ervaring!',
        true, 'Bedankt voor je positieve beoordeling. We zijn blij dat je de workshop waardevol vond!', 101, 103),
       (106, 4.8,
        'Ik heb een geweldige tijd gehad op de workshop. De inhoud was goed gestructureerd en de instructeur was vriendelijk en behulpzaam. Een echte aanrader!',
        true, default, 102, 106),
       (107, 3.5,
        'Prima workshop',
        true, default, 102, 108);

INSERT INTO user_favourite_workshop (user_id, workshop_id)
values (100, 100),
       (100, 103),
       (100, 104),
       (100, 111),
       (100, 108),
       (100, 113),
       (102, 100),
       (102, 101),
       (101, 104);


