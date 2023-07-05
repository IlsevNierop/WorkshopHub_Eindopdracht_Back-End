-- all users have the same password: password
-- Melania (ID 100) is admin

INSERT INTO users (id, first_name, last_name, email, password, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (100, 'Melania', 'Rossi', 'melania@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', false, true, 'pexels-melania-piacquadio-774909.webp' ,'http://localhost:8080/downloadprofilepic/100');
INSERT INTO users (id, first_name, last_name, email, password, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (101, 'Samuel', 'Smith', 'samuel.smith@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        false, true, 'pexels-laker-5792641.webp', 'http://localhost:8080/downloadprofilepic/101');
INSERT INTO users (id, first_name, last_name, email, password, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (102, 'Isabella', 'Janssen', 'isabella.janssen@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', false, true, 'pexels-vinicius-wiesehofer-1130626.webp', 'http://localhost:8080/downloadprofilepic/102');
INSERT INTO users (id, first_name, last_name, email, password, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (103, 'Kees', 'Janssen', 'kees@example.com',
        '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.', false, true, null, null);


INSERT INTO users (id, first_name, last_name, email, password, company_name, kvk_number, vat_number,
                   workshop_owner_verified, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (105, 'Andrea', 'Davis', 'andrea@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Voorbeeld Bedrijf', 12345678, 'NL123456789B01', true, true, true, 'Profielfoto-Andrea.webp', 'http://localhost:8080/downloadprofilepic/105');
INSERT INTO users (id, first_name, last_name, email, password, company_name, kvk_number, vat_number,
                   workshop_owner_verified, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (106, 'Stefan', 'Jansen', 'stefan@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Test Bedrijf', 98765432, 'NL987654321B01', true, true, true, 'pexels-stefan-stefancik-91227.webp', 'http://localhost:8080/downloadprofilepic/106');
INSERT INTO users (id, first_name, last_name, email, password, company_name, kvk_number, vat_number,
                   workshop_owner_verified, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (107, 'Dziana', 'Davis', 'dziana@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Voorbeeld BV', 54321678, 'NL543216789B01', default, true, true, 'pexels-dziana-hasanbekava-7275385.webp', 'http://localhost:8080/downloadprofilepic/107');
INSERT INTO users (id, first_name, last_name, email, password, company_name, kvk_number, vat_number,
                   workshop_owner_verified, workshop_owner, enabled, file_name, profile_pic_url)
VALUES (108, 'Emily', 'Brown', 'emily@example.com', '$2a$12$Ai87GDYBJJ.UFqsat7Fhve7Gz40wOBhKMFuuWYQ4icThJLc2ET4E.',
        'Test BV', 87654321, 'NL876543219B01', default, true, true, 'pexels-monstera-5876695.webp', 'http://localhost:8080/downloadprofilepic/108');

INSERT INTO authorities (user_id, authority)
values (100, 'ROLE_CUSTOMER'),
       (100, 'ROLE_ADMIN'),
       (101, 'ROLE_CUSTOMER'),
       (102, 'ROLE_CUSTOMER'),
       (105, 'ROLE_WORKSHOPOWNER'),
       (105, 'ROLE_CUSTOMER'),
       (106, 'ROLE_CUSTOMER'),
       (106, 'ROLE_WORKSHOPOWNER'),
       (107, 'ROLE_CUSTOMER'),
       (108, 'ROLE_CUSTOMER');


--9 workshops
INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (100, 'Indonesische kook workshop', '2023-10-01', '16:30:00', '20:00:00', 99.0, 'INDOORS', 'Utrecht',
        'Inclusief eten en drinken \n Allergien wordt rekening mee gehouden', 'Ontdek de exotische smaken van Indonesië tijdens onze meeslepende kookworkshop. Stap binnen in een wereld van kruiden, specerijen en traditionele recepten terwijl we je meenemen op een culinaire reis door de Indonesische keuken.
Onze ervaren chef-kok begroet je met een warm welkom en introduceert je in de geheimen van de Indonesische keuken. Je leert authentieke gerechten bereiden, zoals nasi goreng, saté, rendang en gado-gado, waarbij je gebruikmaakt van verse ingrediënten en traditionele kooktechnieken.
Tijdens de workshop krijg je de kans om zelf actief de handen uit de mouwen te steken en te experimenteren met de smaken en texturen van de Indonesische keuken. De chef-kok begeleidt je stap voor stap en deelt zijn/haar kennis over de verschillende kruiden en specerijen die worden gebruikt in de Indonesische gerechten.
Na afloop van de kookworkshop geniet je samen met de andere deelnemers van een heerlijke maaltijd die je zelf hebt bereid. Het is een geweldige gelegenheid om nieuwe mensen te ontmoeten, ervaringen uit te wisselen en te genieten van de verrukkelijke Indonesische gerechten die je hebt gemaakt.
Kom en ontdek de magie van de Indonesische keuken tijdens onze boeiende kookworkshop. Laat je smaakpapillen verwennen en ga naar huis met nieuwe culinaire vaardigheden en een diepgaande waardering voor de rijke eetcultuur van Indonesië.',
        10, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', true, 105, 'Koken.webp', 'http://localhost:8080/downloadworkshoppic/100');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (101, 'Kaarsen maken', '2023-10-03', '13:00:00', '16:00:00', 45.0, 'INDOORS', 'Leiden',
        'Inclusief iets lekkers te eten en drinken \n Je neemt 5 kaarsen mee naar huis', 'Stap in de betoverende wereld van kaarsen maken tijdens onze inspirerende workshop. Laat je creativiteit de vrije loop en ontdek de kunst van het maken van unieke en prachtig geurende kaarsen.
Bij aanvang van de workshop verwelkomen we je in een sfeervolle omgeving, waar de heerlijke geur van verschillende wassen en oliën je tegemoet komt. Onze deskundige kaarsenmaker leidt je door het proces en deelt zijn/haar kennis en tips over het maken van hoogwaardige kaarsen.
Je leert verschillende technieken, zoals gieten, dompelen en vormen, om een verscheidenheid aan kaarsen te creëren. Ontwerp je eigen kleurenpalet, experimenteer met geuren en kies uit een scala aan decoratieve elementen om je kaarsen een persoonlijk tintje te geven.
Tijdens de workshop kun je ook meer te weten komen over de verschillende soorten was en lonten die gebruikt worden in het kaarsen maken. Je krijgt inzicht in de veiligheidsrichtlijnen en de juiste manier om kaarsen te branden en te onderhouden.
Aan het einde van de workshop ga je naar huis met een set prachtige handgemaakte kaarsen, die je kunt gebruiken om je huis te verfraaien of als een uniek cadeau voor vrienden en familie.
Kom en laat je creativiteit opvlammen tijdens onze boeiende workshop Kaarsen maken. Ontdek de magie van het creëren van je eigen kaarsen en geniet van de ontspannende en geurige ambiance die kaarsen brengen.',
        14, 'Kaarsen', null, true, 'ziet er goed uit, mag online', true, 106, 'Kaarsen1.webp', 'http://localhost:8080/downloadworkshoppic/101');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (102, 'Patisserie workshop', '2023-06-15', '11:00:00', '14:00:00', 55.0, 'INDOORS', 'Utrecht',
        'Je krijg je eigengemaakte gebak mee naar huis', 'Welkom bij onze exclusieve 3 uur durende workshop Patisserie in het hart van Utrecht. Deze intieme workshop biedt een unieke gelegenheid voor maximaal 6 deelnemers om onder begeleiding van een ervaren patissier de geheimen van de patisserie te ontdekken.
De workshop vindt plaats in een sfeervolle en goed uitgeruste patisseriekeuken, waar je wordt verwelkomd met een warme glimlach en een kopje koffie of thee. Onze professionele patissier zal je begeleiden bij elke stap van het proces en je voorzien van waardevolle tips en technieken om succesvolle patisseriecreaties te maken.
Gedurende de 3 uur durende workshop leer je een verscheidenheid aan patisserievaardigheden, zoals het maken van perfect krokant deeg, het bereiden van smaakvolle vullingen en het decoreren van je creaties als een ware professional. Je gaat aan de slag met klassieke recepten en leert ook enkele moderne twisten aan te brengen.
De workshop is interactief en hands-on, waarbij elke deelnemer de kans krijgt om actief mee te doen en zijn/haar eigen gebak te creëren. Aan het einde van de workshop is er een gezamenlijke proeverij, waarbij je kunt genieten van de heerlijke resultaten van je inspanningen.
De locatie van de workshop bevindt zich in het bruisende centrum van Utrecht, dicht bij het openbaar vervoer en andere bezienswaardigheden. Het is de perfecte gelegenheid om een culinaire ervaring te combineren met het verkennen van de prachtige stad.
Kortom, deze 3 uur durende Patisserie workshop in Utrecht is een unieke gelegenheid om je vaardigheden op het gebied van patisserie te verbeteren, nieuwe vrienden te maken en te genieten van een smakelijke en gezellige ervaring. Reserveer nu en laat je betoveren door de zoete wereld van de patisserie.',
        6, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', true, 105, 'Bakken--1.webp', 'http://localhost:8080/downloadworkshoppic/102');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (103, 'Macramé plantenhanger maken', '2023-10-01', '14:30:00', '16:30:00', 15.0, 'INDOORS', 'Amsterdam',
        'Er is thee en koffie aanwezig', 'Kom en ontdek de kunst van macramé tijdens onze creatieve 2 uur durende workshop in Amsterdam. Leer hoe je prachtige plantenhangers kunt maken met de oude techniek van knopen, en geef je huis een stijlvolle en groene touch.
De workshop wordt gehouden in een inspirerende locatie in het hart van Amsterdam. Bij aankomst word je verwelkomd met een warme sfeer en alle benodigde materialen voor het maken van je eigen macramé plantenhanger. Onze ervaren instructeur leert je stap voor stap de basistechnieken en de verschillende knopen die je nodig hebt om een prachtig ontwerp te creëren.
Met een kleine groep van maximaal 12 deelnemers is er voldoende ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginner bent of al ervaring hebt met macramé, deze workshop is geschikt voor alle niveaus. Je kunt je eigen creativiteit de vrije loop laten en je plantenhanger personaliseren met verschillende knooppatronen en versieringen.
De kosten voor de workshop bedragen slechts 15 euro per persoon, inclusief alle materialen die nodig zijn om je eigen macramé plantenhanger te maken. Daarnaast krijg je waardevolle tips en tricks van de instructeur, zodat je ook thuis verder kunt gaan met je nieuwe vaardigheden.
Laat je inspireren door de trendy wereld van macramé en ga naar huis met een prachtige handgemaakte plantenhanger om je huis mee op te fleuren. Schrijf je nu in voor deze 2 uur durende workshop in Amsterdam en geniet van een creatieve en ontspannende ervaring samen met gelijkgestemde deelnemers.
', 12, 'Handwerk', 'Breien', true, default, true, 105, 'Macrame.webp', 'http://localhost:8080/downloadworkshoppic/103');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (104, 'Workshop breien voor beginners', '2023-10-08', '10:00:00', '13:00:00', 30.0, 'INDOORS', 'Amsterdam',
        'Ook gevorderden zijn welkom', 'Ontdek de vreugde van breien tijdens onze boeiende 3 uur durende workshop voor beginners in Amsterdam. Of je nu nog nooit een haaknaald hebt vastgehouden of een beetje basiskennis hebt, deze workshop is ontworpen om je de essentiële vaardigheden te leren om prachtige handgemaakte creaties te maken.
De workshop vindt plaats in een gezellige en inspirerende omgeving in het hart van Amsterdam. Bij aankomst ontvang je een warm welkom en alle benodigde materialen om aan de slag te gaan. Onze ervaren instructeur begeleidt je stap voor stap door de basissteken en technieken, en leert je hoe je eenvoudige projecten zoals sjaals, mutsen of amigurumi kunt maken.
Met een kleine groep van maximaal 10 deelnemers is er volop gelegenheid voor persoonlijke begeleiding en interactie. Je krijgt de kans om vragen te stellen, tips te krijgen en te leren van de ervaring van de instructeur. Bovendien is het een geweldige gelegenheid om nieuwe mensen te ontmoeten die dezelfde passie delen.
De kosten voor de workshop bedragen slechts 30 euro per persoon, inclusief alle materialen die je nodig hebt om te breien. Naast de praktische vaardigheden ontvang je ook een handige handleiding met instructies en patronen, zodat je thuis verder kunt oefenen.
Laat je creativiteit de vrije loop en ontdek de ontspannende wereld van breien tijdens deze 3 uur durende workshop. Schrijf je nu in en geniet van een gezellige en leerzame ervaring in Amsterdam. Of je nu een uniek cadeau wilt maken of gewoon wilt ontspannen met een creatieve bezigheid, breien biedt eindeloze mogelijkheden.',
        10, 'Handwerk', 'Breien', true, 'Ziet er interessant uit', true, 106, 'Breien8.webp', 'http://localhost:8080/downloadworkshoppic/104');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (105, 'Workshop keramiek: draaien', '2023-11-01', '14:00:00', '17:00:00', 65.0, 'INDOORS', 'Leiden', default, 'Stap binnen in de fascinerende wereld van keramiek tijdens onze inspirerende 3 uur durende workshop: Draaien. Deze exclusieve workshop, gehouden in Haarlem, biedt een intieme setting voor maximaal 6 deelnemers om de kunst van het keramiekdraaien te verkennen.
De workshop vindt plaats in een prachtig atelier, waar je wordt verwelkomd met een warme sfeer en een deskundige keramist als je gids. Je leert de basisprincipes van het draaien op een draaischijf en krijgt hands-on begeleiding om je vaardigheden te ontwikkelen en te verfijnen.
Met een kleine groep van maximaal 6 deelnemers is er voldoende ruimte voor persoonlijke aandacht en begeleiding. De ervaren keramist zal je stap voor stap door het proces leiden, je helpen bij het centreren van de klei, het vormgeven van je creaties en het creëren van prachtige keramische stukken.
De workshop biedt een unieke gelegenheid om je eigen keramiek te creëren, zoals kommen, mokken, vazen en meer. Je kunt experimenteren met verschillende vormen, texturen en decoratieve technieken om een persoonlijk en uniek stuk te maken.
De kosten voor de workshop bedragen 65 euro per persoon, inclusief alle materialen en het bakken van je keramische creaties. Aan het einde van de workshop worden je werken gebakken en kun je ze op een later tijdstip ophalen om ze te glazuren en af te werken.
Laat je creativiteit de vrije loop en geniet van een unieke ervaring met de workshop Keramiek: Draaien. Reserveer nu je plek in deze exclusieve workshop en laat je inspireren door de magie van het keramiekdraaien.',
        8, 'Keramiek', null, true, 'ziet er goed uit, mag online', true, 105, 'Keramiek1.webp', 'http://localhost:8080/downloadworkshoppic/105');


INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (106, 'Eetbare paddenstoelen', '2023-11-13', '15:00:00', '16:30:00', 25.0, 'OUTDOORS', 'Woerden',
        'Inclusief een klein paddenstoelen hapje', 'Welkom bij onze boeiende 1,5 uur durende workshop Eetbare Paddenstoelen in de prachtige buitenomgeving van Woerden. Tijdens deze workshop leer je alles over de fascinerende wereld van eetbare paddenstoelen en ontdek je hoe je ze kunt identificeren en gebruiken in je eigen culinaire creaties.
De workshop vindt plaats in een sfeervolle buitenlocatie in Woerden, waar je wordt omringd door de natuurlijke pracht van het landschap. Onze deskundige gids, een ervaren paddenstoelenkenner, neemt je mee op een informatieve en interactieve wandeling door het bos, waar je leert hoe je eetbare paddenstoelen kunt herkennen, plukken en gebruiken.
Met een kleine groep van maximaal 8 deelnemers is er voldoende ruimte voor persoonlijke aandacht en interactie met de gids. Je krijgt waardevolle tips en technieken om veilig en verantwoord eetbare paddenstoelen te verzamelen, en je leert over de verschillende soorten paddenstoelen en hun culinaire toepassingen.
De kosten voor de workshop bedragen slechts 25 euro per persoon, inclusief begeleiding door de paddenstoelenexpert en informatiemateriaal om mee naar huis te nemen. Het is een geweldige kans om je kennis over eetbare paddenstoelen uit te breiden en een nieuwe dimensie toe te voegen aan je culinaire avonturen.
Kom en sluit je aan bij onze inspirerende workshop Eetbare Paddenstoelen in Woerden. Leer over de wonderen van de natuur, ontdek nieuwe smaken en laat je inspireren door de mogelijkheden van eetbare paddenstoelen. Reserveer nu je plek en maak deel uit van deze unieke en smakelijke ervaring.',
        8, 'Wildplukken', 'Natuur', default, default, default, 106, 'Wildplukken2.webp', 'http://localhost:8080/downloadworkshoppic/106');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (107, 'Creatief schilderen', '2023-05-01', '18:30:00', '21:30:00', 35.0, 'INDOORS', 'Utrecht',
        'Inclusief eten en drinken \n Inclusief materialen', 'Welkom bij onze inspirerende 3 uur durende workshop Creatief Schilderen in het bruisende Utrecht. Deze workshop biedt een uitgelezen kans voor maximaal 8 deelnemers om hun creativiteit te uiten en nieuwe schildervaardigheden te ontdekken.
De workshop vindt plaats in een sfeervol atelier in Utrecht, waar je wordt verwelkomd met een warme ambiance en alle benodigde materialen om aan de slag te gaan. Onze ervaren kunstenaar begeleidt je stap voor stap door het creatieve proces, waarbij je de vrijheid hebt om je eigen stijl en interpretatie te ontwikkelen.
Met een kleine groep van maximaal 8 deelnemers is er volop ruimte voor persoonlijke begeleiding en interactie. Of je nu een beginnende kunstenaar bent of al wat ervaring hebt, deze workshop is geschikt voor alle niveaus. Je leert verschillende technieken, kleurenpaletten en compositieprincipes om expressieve en unieke schilderijen te creëren.
De kosten voor de workshop bedragen slechts 35 euro per persoon, inclusief alle schildermaterialen en een canvas om je kunstwerk op te maken. Je krijgt ook waardevolle feedback en begeleiding van de kunstenaar om je te helpen je creatieve vaardigheden verder te ontwikkelen.
Laat je verbeelding de vrije loop en geniet van een ontspannende en inspirerende schilderervaring tijdens deze 3 uur durende workshop. Schrijf je nu in en maak deel uit van een creatieve gemeenschap in Utrecht, waar je kunt ontdekken, leren en je eigen kunstwerken kunt creëren.',
        8, 'Schilderen', null, true, 'ziet er goed uit, mag online', default, 105, 'Schilderen3.webp', 'http://localhost:8080/downloadworkshoppic/107');

INSERT INTO workshops (id, title, date, start_time, end_time, price, in_or_outdoors, location, highlighted_info,
                       description, amount_of_participants, workshop_category1, workshop_category2, workshop_verified,
                       feedback_admin, publish_workshop, workshop_owner_id, file_name, workshop_pic_url)
VALUES (108, 'Taarten bakken', '2023-09-05', '10:00:00', '13:00:00', 42.50, 'INDOORS', 'Amsterdam', default, 'Stap in de heerlijke wereld van taarten bakken tijdens onze smakelijke 3 uur durende workshop in Amsterdam. Deze workshop biedt een unieke gelegenheid voor maximaal 8 deelnemers om te leren hoe ze prachtige en verrukkelijke taarten kunnen maken.
De workshop wordt gehouden in een gezellige bakkerij in Amsterdam, waar je wordt begroet met de heerlijke geur van versgebakken lekkernijen. Onze ervaren banketbakker zal je begeleiden bij elke stap van het proces, van het bereiden van het deeg tot het maken van de perfecte vullingen en decoraties.
Met een kleine groep van maximaal 8 deelnemers is er voldoende ruimte voor persoonlijke aandacht en interactie. Je leert verschillende technieken en krijgt waardevolle tips en trucs om je taarten naar een hoger niveau te tillen. Of je nu een beginner bent of al wat ervaring hebt, deze workshop is geschikt voor alle niveaus.
De kosten voor de workshop bedragen 42,50 euro per persoon, inclusief alle ingrediënten en materialen die nodig zijn om je eigen taarten te maken. Je gaat naar huis met nieuwe vaardigheden, een schat aan kennis en natuurlijk je zelfgemaakte taarten om te delen en van te genieten.
Laat je creativiteit en smaakpapillen de vrije loop tijdens deze 3 uur durende workshop Taarten Bakken. Schrijf je nu in en ontdek de geheimen van het maken van heerlijke taarten in een gezellige en leerzame omgeving.',
        8, 'Koken', 'Bakken', true, 'ziet er goed uit, mag online', default, 105, 'Bakken-.webp', 'http://localhost:8080/downloadworkshoppic/108');

INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (101, '2023-06-01', 'Geweldige workshop!', 2, 101, 102, 90.0);
INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (102, '2023-05-29', 'Ik kijk ernaar uit! En ben vegetarier.', 1, 101, 102, 45.0);
INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (103, '2023-05-30', 'Leuk concept!', 3, 103, 100, 45.0);
INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (104, '2023-06-01', 'Ik breng twee vrienden mee!', 3, 102, 101, 105.0);
INSERT INTO bookings (id, date_order, comments_customer, amount, workshop_id, customer_id, total_price)
VALUES (105, '2023-05-31', 'Interessante workshop! Wij moeten een half uurtje eerder weg', 2, 104, 100, 50.0);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (100, 4.5,
        'De workshop was echt geweldig! Ik heb zoveel geleerd en de docent was zeer behulpzaam. Een aanrader!', default,
        'Bedankt voor je positieve feedback!', 102, 101);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (101, 3.2,
        'De workshop was redelijk, maar ik had verwacht dat het interactiever zou zijn. De inhoud was wel interessant.',
        true, 'Dank je wel voor je feedback. We zullen je opmerkingen in overweging nemen.', 102, 102);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (102, 5.0,
        'Ik ben zeer tevreden met de workshop! Het was inspirerend en de sfeer was geweldig. Ik raad het iedereen aan!',
        true, 'We zijn blij dat je ervan genoten hebt. Bedankt voor je positieve woorden!', 101, 101);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (103, 2.8,
        'Helaas voldeed de workshop niet aan mijn verwachtingen. De inhoud was te oppervlakkig en ik vond de organisatie slecht.',
        false, 'Onze excuses voor het ongemak. We zullen je opmerkingen gebruiken om onze service te verbeteren.', 102,
        107);

INSERT INTO reviews (id, rating, review_description, review_verified, feedback_admin, workshop_id, customer_id)
VALUES (104, 4.7,
        'De workshop was fantastisch! Ik heb zoveel geleerd en het heeft mijn creatieve vaardigheden echt verbeterd. Een geweldige ervaring!',
        default, 'Bedankt voor je geweldige feedback. We zijn blij dat je tevreden bent met de workshop!', 102, 100);


