## **Документация**
1. #### <a name="_toc167561307"></a><a name="_toc167912678"></a> **Использование Аннотации**
Главный класс помечается аннотацией *@RestApiGenerator* с параметром «*jsonPath*», в котором указывается полный путь к JSON файлу. Кроме того, требуется использование обычных аннотаций *Spring-Boot* приложения (листинг 1).

Листинг 1 — Использование аннотации.

|<p>@SpringBootApplication</p><p>@RestApiGenerator(jsonPath="JsonFileFullPath")</p><p>public class Main {</p><p>`    `public static void main(String[] args) {</p><p>`       `SpringApplication.run(Main.class,args);</p><p>`    `}</p><p>}</p>|
| :- |
1. #### **Группы конечных точек**
Файл в формате JSON имеет структуру, изображенную в листинге 2.

«*EndpointGroup1*» и «*EndpointGroup2*» — это названия групп конечных точек, на основе которых будут сгенерированы контроллеры в формате: «*EndpointGroupController*» и репозитории в формате: «*EndpointGroupRepository*».[ «*http*](#_heading=h.2p2csry)» - место размещения информации о конечных точках.

Листинг 2 — Формат файла JSON.

|<p>{</p><p>`  `"EndpointGroup1": {</p><p>`    `"http": {</p><p>`    `}</p><p>`  `},</p><p>`   `"EndpointGroup2": {</p><p>`    `"http": {</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |
##### ***http\_prefix***
Листинг 3 — Использование «*http\_prefix*».

|<p>{</p><p>`  `"EndpointGroup": {</p><p>`  `"http\_prfix":"prefix/",</p><p>`   `"http": {</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

При использовании параметра «*http\_prefix*» (листинг 3), к которому принадлежит *«EndpointGroup*» группа конечных точек, появляется аннотация: *@RequestMapping(“prefix/”).*
1. #### **Конечные точки**
Листинг 4 — Формат конечной точки.

|<p>"http": {</p><p>`     `"EndpointName1": {</p><p>`        `"type":"someType",</p><p>`        `"request": "endpoint"</p><p>`      `},</p><p>`      `"EndpointName2": {</p><p>`        `"type":"someType",</p><p>`        `"request": "endpoint"</p><p>`      `} </p><p>}</p>|
| :- |

Конечные точки размещаются в поле «*http*» (листинг 4) «*EndpointName1*» и «*EndpointName2*» — это названия конечных точек, на основе которых будут сгенерированы функции в контроллерах и репозиториях в формате: <Тип><Имя конечной точки>. *«type»* - тип запроса. *«request»* - место размещения самой конечной точки, при парасанге которой будет сгенерирован запрос к РСУБД.
1. #### **Тип запроса**
Тип запроса размещается в конечной точке и может иметь следующие форматы (листинг 5–8).

Листинг 5 — Формат типа запроса.

|<p>"EndpointName1": {</p><p>`    `"get":"entity->limit|sort|fields",</p><p>`    `"post":"entity->entity",</p><p>`    `"put":"entity->entity",</p><p>`    `"patch":"entity->entity",</p><p>`    `"delete":"",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В листинге 5 в *«EndpointName1»* все типы запроса относятся к одной и той же конечной точке. Использование всех пяти типов в конечной точке не обязательно.

Листинг 6 — Формат типа запроса.

|<p>"EndpointName2": {</p><p>`    `"type":"get:entity",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName3": {</p><p>`    `"type":"post:entity->entity",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName4": {</p><p>`    `"type":"put:entity->entity",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName5": {</p><p>`    `"type":"patch:entity->entity",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName6": {</p><p>`    `"type":"delete",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В «EndpointName2», «EndpointName3», «EndpointName4», «EndpointName5», «EndpointName6» используется один тип запроса на одну конечную точку (листинг 6).

Листинг 7 — Формат типа запроса.

|<p>"EndpointName7": {</p><p>`    `"types":{</p><p>`        `"get":{},</p><p>`        `"post":{},</p><p>`        `"put":{},</p><p>`        `"delete":{},</p><p>`        `"patch":{}</p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName7»* все типы запроса относятся к одной и той же конечной точке. Использование всех пяти типов в конечной точке не обязательно. В этом формате можно указать более расширенную информацию о каждом типе (листинг 7).

Листинг 8 —  Формат типа запроса.

|<p>"EndpointName8": {</p><p>`   `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName8»* по умолчанию будет тип запроса *GET* (листинг 8).

Использование двух форматов в одной и той же конечной точке недопустимо, так как будет сформирована ошибка времени компиляции.
##### **Тип запроса *GET***
В типе запроса по умолчанию *GET* (листинг 9–10), в случае успеха, будет возвращен код 200, а метод в контроллере помечен аннотацией: @ResponseStatus (HttpStatus.OK).

В *«EndpointName1»* и *«EndpointName2»* *«field1|field2|field3»* (листинг 9) — это список возвращаемых полей (для *GET* они не обязательные). Поля перечисляются через «|» с учетом их псевдонимов. Через «->» указываются используемые дополнительные параметры (также не обязательные).

Листинг 9 —  Тип запроса *GET*.

|<p>"EndpointName1": {</p><p>`     `"get":"field1|field2|field3->limit|sort|fields",</p><p>`     `"request": "endpoint"</p><p>},</p><p>"EndpointName2": {</p><p>`    `"type":"get:field1|field2|field3",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

Листинг 10 —  Тип запроса *GET.*

|<p>"EndpointName3": {</p><p>`    `"types":{</p><p>`        `"get":{</p><p>`        `"entity":"field1|field2,</p><p>`        `"return":"limit|sort|fields",</p><p>`        `"httpOk":"OK"</p><p>`        `}</p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName3»* параметр *«entity»* — это возвращаемые поля (для *GET* этот параметр не обязательный). Поля перечисляются через «|» с учетом их псевдонимов. В *«return»* указываются используемые дополнительные параметры (также не обязательные). В *«httpOk»* указывается *org.springframework.http.HttpStatus*.
###### **Параметр *«limit»***
В случае указания параметра *«limit»* генерируется код (листинг 11).

Листинг 11 —  Код при использовании *«limit».*

|<p>@Min(0) @RequestParam(defaultValue = "9223372036854775807") </p><p>@Parameter(name = "limit", example = "1") long limit,</p><p>@Min(0) @RequestParam(defaultValue = "0") long offset </p>|
| :- |

Как видно из листинга 11, оба сгенерированных параметра являются необязательными. В случае обращения к РСУБД, в конце запроса будет добавлен *«limit»* и *«offset».*
###### **Параметр *«sort»***
В случае указания параметра *«sort»* генерируется код (листинг 12).

Листинг 12 —  Код при использовании *«sort».*

|<p>@RequestParam @Parameter(name = "sort", example = "fieldName") </p><p>String sort,</p><p>@RequestParam(defaultValue = "true") @Parameter(name = "asc", </p><p>example = "true", description = "asc=true, desc=false") boolean asc</p>|
| :- |

Как видно из Листинга 12, сгенерированный параметр *«sort»* является обязательным, а *«asc»* не обязательным. Он имеет тип *boolean,* и, в случае *true* будет *ASC*, а в случае *false DESC.* Параметр *«sort»* — это имя поля для функции *ORDER BY*. В случае обращения к РСУБД, в конце запроса перед *LIMIT* будет добавлен *ORDER BY.*
###### **Параметр *«fields»***
В параметре *«fields»* перечисляются возвращаемые поля. Параметр не обязателен, и, в случае его отсутствия, возвращаются поля, перечисленные в *«entity».* В случае передачи полей, которых нет в *«entity»,* они не будут возвращены запросом. Если полей в *«entity»* вообще не будет, будут возвращены все поля, переданные в параметре *«fields».*
##### **Тип запроса *POST***
В типе запроса *POST* по умолчанию (листинг 13–14) в случае успеха, будет возвращен код 201, а метод в контроллере помечен аннотацией: *@ResponseStatus (HttpStatus.CREATED).*

Листинг 13 — Тип запроса *POST*.

|<p>"EndpointName1": {</p><p>` `"post":"fieldStr1-s=str|field2-i=23|field3-b=true->field1|field2",</p><p>`   `"request": "endpoint"</p><p>},</p><p>"EndpointName2": {</p><p>`    `"type":"post:fieldStr1-s=str|field2-i=23|field3-b=true",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

*В «EndpointName1» и «EndpointName2»* (листинг 13) *«fieldStr1-s=str|field2-i=23|field3-b=true»—* это список полей для вставки (для *POST* они обязательные). Поля перечисляются через «|» с учетом их псевдонимов в формате *«fieldType = DEFAULT».  «DEFAULT»* по умолчанию *null*; *«DEFAULT»* будет вставлено в поле, если оно будет отсутствовать. Через «->» указываются возвращаемые поля.

Листинг 14 — Тип запроса *POST.*

|<p>"EndpointName3": {</p><p>`    `"types":{</p><p>`        `"post":{</p><p>`        `"entity":"field1-s=str|field2,</p><p>`        `"return":"field1|field2",</p><p>`        `"httpOk":"CREATED"</p><p>`        `}</p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName3»* параметр *«entity»* (листинг 14) — это вставляемые поля (для *POST* этот параметр обязательный). Поля перечисляются через «|» с учетом их псевдонимов. В *«return»* указываются возвращаемые поля. В *«httpOk»* указывается *«enum»* класса *org.springframework.http.HttpStatus.*
##### **Тип запроса *PUT***
В типе запроса *PUT* по умолчанию (листинг15-16), в случае успеха, будет возвращен код 200, а метод в контроллере помечен аннотацией: *@ResponseStatus (HttpStatus.OK).*

Листинг 15 —  Тип запроса *PUT*.

|<p>"EndpointName1": {</p><p>`    `"put":"fieldStr1-s=str|field2-i=23|field3-b=true->field1|field2",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName2": {</p><p>`    `"type":"put:fieldStr1-s=str|field2-i=23|field3-b=true",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName1»* и *«EndpointName2»* (листинг 15) *«fieldStr1-s=str|field2-i=23|field3-b=true»* — это список полей для вставки (для *PUT* они обязательные). Поля перечисляются через «|» с учетом их псевдонимов в формате *«fieldType = DEFAULT».* *«DEFAULT»* по умолчанию *«null»; «DEFAULT»* будет обновлять поле, если оно будет отсутствовать. Через «->» указываются возвращаемые поля.

Листинг 16 —  Тип запроса *PUT.*

|<p>"EndpointName3": {</p><p>`    `"types":{</p><p>`        `"post":{</p><p>`        `"entity":"field1-s=str|field2,</p><p>`        `"return":"field1|field2",</p><p>`        `"httpOk":"OK"</p><p>`        `}</p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName3»* параметр *«entity»* (листинг 16) — это обновляемые поля (для *PUT* этот параметр обязательный). Поля перечисляются через «|» с учетом их псевдонимов. В *«return»* указываются возвращаемые поля. В *«httpOk»* указывается *«enum»* класса *org.springframework.http.HttpStatus.*
##### **Тип запроса *PATCH***
В типе запроса *PATCH* по умолчанию (листинг 16–17), в случае успеха, будет возвращен код 200, а метод в контроллере помечен аннотацией: @*ResponseStatus (HttpStatus.OK).*

Листинг 17 — Тип запроса *PATCH.*

|<p>"EndpointName1": {</p><p>`    `"patch":"field1|field2|field3->field1|field2",</p><p>`    `"request": "endpoint"</p><p>},</p><p>"EndpointName2": {</p><p>`    `"type":"patch:field1|field2|field3",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName1»* и *«EndpointName2»* (листинг 17) *«field1|field2|field3*» — это список полей для вставки (для *PATCH* они обязательные). Поля перечисляются через «|», с учетом их псевдонимов. Через «->» указываются возвращаемые поля.

Листинг 18 — Тип запроса *PATCH.*

|<p>"EndpointName3": {</p><p>`    `"types»: {</p><p>`        `"patch»: {</p><p>`        `"entity":"field1-s=str|field2,</p><p>`        `"return":"field1|field2",</p><p>`        `"httpOk":"OK"</p><p>`        `}</p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

В *«EndpointName3»* параметр *«entity»* (листинг 18) — это обновляемые поля (для *PATCH* этот параметр обязательный). Поля перечисляются через «|», с учетом их псевдонимов. В *«return»* указываются возвращаемые поля. В *«httpOk»* указывается *«enum»* класса *org.springframework.http.HttpStatus.*
##### **Тип запроса *DELETE***
В типе запроса *DELETE* по умолчанию (листинг 19), в случае успеха, будет возвращен код 204, а метод в контроллере помечен аннотацией: *@ResponseStatus (HttpStatus.NO\_CONTENT).*

Листинг 19 — Тип запроса *DELETE.*

|"EndpointName1": {<br>`  `"delete":"",<br>`  `"request": "endpoint"<br>},<br>"EndpointName2": {<br>`  `"type":"delete",<br>`  `"request": "endpoint"<br>},<br>"EndpointName3": {<br>`  `"types":{<br>`    `"delete":{<br>`      `"httpOk":"NO\_CONTENT"<br>`    `}<br>`  `},<br>`  `"request": "endpoint"<br>}|
| :- |

В *«httpOk»* указывается *«enum»* класса *org.springframework.http.HttpStatus.*
1. #### **Тип данных**
Поддерживаются 6 типов данных (таблица 1).

Таблица 1 — Типы данных.

|Тип.|Обозначение.|
| - | - |
|*String*|*-s*|
|*Double*|*-d*|
|*Boolean*|*-b*|
|*Float*|*-f*|
|*Long*|*-l*|
|*Integer*|*-i*|
1. #### * **Предикаты**
Поддерживаются 12 предикатов (таблица 2).

Таблица 2 — Предикаты.

|*SQL.*|Обозначение.|
| - | - |
|*==*|*eq\_*|
|*!=*|*ne\_*|
|*>*|*gt\_*|
|*<*|*lt\_*|
|*>=*|*ge\_*|
|*=<*|*le\_*|
|*LIKE*|*like\_*|
|*LIKE REGEX*|*reg\_*|
|*IN*|*in\_*|
|*NOT LIKE*|*not\_like\_*|
|*NOT LIKE REGEX*|*not\_reg\_*|
|*NOT IN*|*not\_in\_*|
1. #### **Применение предиката к полю**
Формат применения предиката имеет вид: <Предикат><Имя поля><Тип> пример представлен в листинге 20.

Листинг 20 — Применение предиката.

|"like\_FieldName-s"|
| :- |

Применить *LIKE* к полю с именем *«FieldName»,* у которого тип *String*. Предикаты могут указываться как в фильтрах, так и в запросе. Сгенерированный участок кода примет форму (листинг 21).

Листинг 21— Применение предиката. Сгенерированный код.

|DSL.field("FieldName").like(like\_FieldName)|
| :- |

*«like\_FieldName»* - имя переменной в параметрах для фильтра или *«PathVariable»*. По умолчанию предикат это *«eq\_»,* а тип *Long*. В случае отсутствия предиката имя переменной будет *«FieldName»*.
1. #### **Запрос к РСУБД**
Конечная точка размещается в поле *«request»* и имеет формат:

Листинг 22 — Формат конечной точки.

|"request":"Table1/{like\_F1-s}|{le\_F2}&({ne\_F3}|{F5})/{F3}"|
| :- |

Для листинга 22, в случае типа *GET*, будет сформирован запрос к РСУБД, находящийся в листинге 23. 

Листинг 23— Запрос к РСУБД для листинга 22.

|<p>SELECT \* FROM TABLE1</p><p>WHERE (F1 LIKE ? OR (F2<=? AND (F3!=? OR F5=?))) AND (F3=?); </p>|
| :- |

Внутри фигурных скобок «{}» находятся поля таблицы *«Table1»,* а в квадратных скобках «[]» размещаются фильтры, которые могут быть применены к этим полям. Возможно также составление условий из полей и фильтров. Если фильтр не содержит параметров, он не будет оказывать влияние на остальные условия. Для условий поддерживаются операторы «&»-«и» и «|»-«или», а также возможно использование скобок «()». Косая черта «/» между полями обозначает операцию «и» и имеет самый низкий приоритет.
##### **Адрес**
Листинг 24 — *«request».*

|"request":"Table1/{f1-s}/Table2/{f2-s}/Table3/{f3-s}/Table4"|
| :- |

В запросе может принимать участие любое число таблиц. Библиотека поддерживает следующие виды связей: один ко многим; многие к одному; многие ко многим. Связи между таблицами указывается в параметрах *«joins»*

Часть запроса, относящегося ко всем таблицам, кроме двух последних таблиц для *POST* и последней для остальных, выглядит одинаково по структуре.

Для листинга 24 эта часть будет выглядеть как в листинге 25.

Листинг 25 — Часть запроса к РСУБД, относящейся к 1 части запроса листинга 24.

|<p>SELECT ID FROM TABLE3 WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2 WHERE TABLE1\_ID IN (</p><p>SELECT ID FROM TABLE1 WHERE (F1=?)) AND (F2=?)) AND (F3=?)</p>|
| :- |

Формируется *SELECT* из *IN*, в котором находится *SELECT* выбирающий поле, которое в свою очередь является связью с предыдущей таблицей в запросе, а также условием выбора, соединенного через «и». 

![Изображение выглядит как текст, снимок экрана, Шрифт, диаграмма

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.001.jpeg)

Рисунок 1 — Схема 1.

###### **Запрос *GET***
Листинг 26 — Запрос *GET*.

|<p>"get":"F5|I5",</p><p>"request":"Table2/{f2-s}/Table3/{f3-s}/Table4/{f4-s}/Table5/{f5-s}/"</p>|
| :- |

Схема базы данных, к которой относится этот запрос из листинга 26 изображена на рисунке 1.

Листинг 27 — Запроса к РСУБД, относящейся к листингу 26.

|<p>SELECT F5, I5 FROM TABLE5 WHERE TABLE4\_ID IN(</p><p>SELECT ID FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2 WHERE</p><p>(F2=?)) AND (F3=?)) AND (F4=?)) AND F5=?;</p>|
| :- |

В *GET* запросе, часть, относящаяся к адресу — это все таблицы, кроме последней. К первой таблице относится *SELECT* с полями, перечисленными в типе запроса.
###### **Запрос *POST***
В запросе *POST* можно вернуть поля, но только относящиеся к последней таблице. Вне зависимости от того существует ли выборка первой части или нет все равно будет вставлена хотя бы одна запись.

**Одна таблица**

Листинг 28 — Запрос *POST*. Случай 1 таблица.

|<p>"post":"F5|I5",</p><p>"request":"Table5/"</p>|
| :- |

Схема базы данных, к которой относится этот запрос из листинга 28 изображена на рисунке 1. 

Листинг 29 — Запроса к РСУБД, относящейся к листингу 28.

|INSERT INTO TABLE5 (F5, I5) VALUES(?, ?);|
| :- |

**Один ко многим**

Листинг 30 — Запрос *POST*. Случай один ко многим.

|<p>"post":"F5|I5",</p><p>"request":"Table3/{f3-s}/Table4/{f4-s}/Table5/"</p>|
| :- |

Схема базы данных, к которой относится этот запрос из листинга 30 изображена на рисунке 1.

Листинг 31 — Запроса к РСУБД, относящейся к листингу 30.

|<p>INSERT INTO TABLE5 (TABLE4\_ID, F5, I5) (SELECT NULL, ?, ?</p><p>WHERE NOT EXISTS (</p><p>SELECT ID, ?, ? FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE (F3=?)) AND (F4=?)) </p><p>UNION ALL(</p><p>SELECT ID, ?, ? FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3</p><p>WHERE (F3=?)) AND (F4=?)));</p>|
| :- |

**Многие ко многим**

Листинг 32 — Запрос *POST*. Случай многие ко многим.

|<p>"post":"F5|I5",</p><p>"request":"Table3/{f3-s}/Table4/{f4-s}/Table5/"</p><p>"pseudonyms": {</p><p>`    `"joins": {</p><p>`      `"Table4:Table3":["Table4\_has\_Table5"]</p><p>`    `} </p><p>}</p>|
| :- |

Схема базы данных, к которой относится запрос из листинга 32 изображена на рисунке 2. Она состоит из 2-х частей (листинг 33–34).

Листинг 33 — Запроса к РСУБД, относящейся к листингу 32. Первая часть.

|INSERT INTO TABLE5 (F5, I5) VALUES(?, ?) RETURNING ID as "RESULT\_ID";|
| :- |

Листинг 34. Запроса к РСУБД, относящейся к листингу 32. Вторая часть.

|<p>INSERT INTO TABLE4\_HAS\_TABLE5 (TABLE4\_ID, TABLE5\_ID) (</p><p>SELECT RESULT\_ID,? FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE (F3=?)) AND (F4=?));</p>|
| :- |

![Изображение выглядит как текст, снимок экрана, диаграмма, Шрифт

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.002.jpeg)

Рисунок 2 — Схема 2.


**Многие к одному**

Листинг 35 — Запрос *POST*. Случай многие ко одному.

|<p>"post":"F5|I5",</p><p>"request":"Table3/{f3-s}/Table4/{f4-s}/>Table5/"</p>|
| :- |

«*>*» — это отображение типа связи многие к одному. Схема базы данных, к которой относится запрос из листинга 35 изображена на рисунке 3. Он состоит из 2-х частей (листинг 36–37).

Листинг 36 — Запроса к РСУБД, относящейся к листингу 35. Первая часть.

|INSERT INTO TABLE5 (F5, I5) VALUES(?, ?) RETURNING ID AS "RESULT\_ID";|
| :- |

Листинг 37. Запроса к РСУБД, относящаяся к листингу 32. Вторая часть.

|<p>UPDATE TABLE4 SET TABLE5\_ID= RESULT\_ID </p><p>WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 </p><p>WHERE (F3=?)) AND (F4=?);</p>|
| :- |

![Изображение выглядит как текст, снимок экрана, диаграмма, Шрифт

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.003.jpeg)

Рисунок 3 — Схема 3.
###### **Запрос *PUT***
Листинг 38 — Запрос *PUT.*

|<p>"put":"F5=str|I5=10",</p><p>"request":"Table2/{f2-s}/Table3/{f3-s}/Table4/{f4-s}/Table5/{f5-s}"</p>|
| :- |

Схема базы данных, к которой относится запрос из листинга 38 изображена на рисунке 1.  Запрос к РСУБД находится в листинг 39.

Листинг 39 — Запроса к РСУБД, относящейся к листингу 38.

|<p>UPDATE TABLE5 SET F5=?, I5=?</p><p>WHERE TABLE4\_ID IN (</p><p>SELECT ID FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2 WHERE</p><p>(F2=?)) AND (F3=?)) AND (F4=?)) AND F5=?;</p>|
| :- |

В *PUT* запросе, часть, относящаяся к адресу — это все таблицы, кроме последней. К первой таблице относится *PUT* с *IN* вместе *SELECT* к предыдущем таблицам и условиям выбора. В запросе *PUT*, в случае не передачи какого-то значения, будет вставлено значение по умолчанию. Если его нет, то null. В данном случае *«F5=“str”»,* *«I5=10».*
###### **Запрос *PATCH***
Листинг 40 — Запрос *PATCH*.

|<p>"patch":"F5|I5",</p><p>"request":"Table2/{f2-s}/Table3/{f3-s}/Table4/{f4-s}/Table5/{f5-s}"</p>|
| :- |

Схема базы данных, к которой относится запрос из листинга 40 изображена на рисунке 13. Запрос к РСУБД изображен в листинг 41–42.

Листинг 41— Запроса к РСУБД, относящейся к листингу 40. Все параметры есть.

|<p>UPDATE TABLE5 SET F5=?, I5=?</p><p>WHERE TABLE4\_ID IN (</p><p>SELECT ID FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2 WHERE</p><p>(F2=?)) AND (F3=?)) AND (F4=?)) AND F5=?;</p>|
| :- |

В *PATCH* запросе, часть, относящаяся к адресу — это все таблицы, кроме последней. К первой таблице относится *PATCH* с *IN* вместе с *SELECT* к предыдущим таблицам и условиям выбора. В запросе *PATCH*, в случае не передачи какого-то значения поля, будет сформирован запрос, находящийся в листинге 42 (не передано *«F5»*).

Листинг 42 — Запроса к РСУБД, относящейся к листингу 40. Нет *«F5».*

|<p>UPDATE TABLE5 SET F5=F5, I5=?</p><p>WHERE TABLE4\_ID IN (</p><p>SELECT ID FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2 WHERE</p><p>(F2=?)) AND (F3=?)) AND (F4=?)) AND F5=?;</p>|
| :- |
###### **Запрос *DELETE***
Листинг 43 — Запрос *DELETE.*

|<p>"type":"delete",</p><p>"request":"Table3/{f3-s}/Table4/{f4-s}/Table5/{f5-s}"</p>|
| :- |

Схема базы данных, к которой относится запрос из листинга 43, изображена на рисунке 1. 

Листинг 44 — Запроса к РСУБД, относящейся к листингу 43.

|<p>DELETE FROM TABLE5 WHERE TABLE4\_ID IN (</p><p>SELECT ID FROM TABLE4 WHERE TABLE3\_ID IN (</p><p>SELECT ID FROM TABLE3 WHERE </p><p>(F3=?)) AND (F4=?)) AND F5=?;</p>|
| :- |

В *DELETE* запросе, часть, относящаяся к адресу — это все таблицы, кроме последней. К первой таблице относится *DELETE* с *IN* вместе c *SELECT* предыдущей таблице и условием выбора.
1. #### **Псевдонимы**
   ###### **«joins»**
По умолчанию поля связаны типом связи один ко многим.

Листинг 45 — Обозначение связи для таблиц в запросе.

|<p>{</p><p>"GroupDefault":{</p><p>` `"http":{</p><p>`       `"endpointTable2ToTable1":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`      `"endpointTable1ToTable2":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`      `"endpointManyToOne":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/>table2"</p><p>`      `},</p><p>`       `"endpointOneToMany":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/<table2"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Для *«endpointTable2ToTable1»* будет сгенерирован запрос к РСУБД (листинг 46).

Листинг 46. Запроса к РСУБД, относящейся к листингу 45. Для *“endpointTable2ToTable1”.*

|<p>SELECT \* FROM TABLE2 </p><p>WHERE TABLE1\_ID IN (</p><p>SELECT ID FROM TABLE1);</p>||
| :- | :- |
Однако и для «*endpointTable1ToTable2»* будет сгенерирован запрос к РСУБД (листинг 47).

Листинг 47 — Запроса к РСУБД, относящейся к листингу 45. Для «endpointTable1ToTable2».

|<p>SELECT \* FROM TABLE1 </p><p>WHERE TABLE2\_ID IN (</p><p>SELECT ID FROM TABLE2);</p>|
| :- |

Однако обычно 2 таблицы так не связанны. Тип связи один ко многим или многие к одному можно указать в самом запросе «<» или «>» - этот знак указывается перед таблицей и обозначает тип связи (в сгенерированной конечной точке он будет отсутствовать).

Так для *«endpointManyToOne»,* код обращения к РСУБД представлен в листинге 48.

Листинг 48 — Запроса к РСУБД, относящейся к листингу 45. Для «endpointManyToOne».

|<p>SELECT \* FROM TABLE1 </p><p>WHERE TABLE2\_ID IN(</p><p>SELECT ID FROM TABLE2);</p>|
| :- |

А для *«endpointOneToMany»,* код обращения к РСУБД представлен в листинге 49.

Листинг 49 — Запроса к РСУБД, относящейся к листингу 45. Для *«endpointOneToMany».*

|<p>SELECT \* FROM TABLE2</p><p>WHERE TABLE1\_ID IN (</p><p>SELECT ID FROM TABLE1);</p>|
| :- |

**Поля связи таблиц**

Схема базы данных, к которой относится запрос из листинга 50 изображена рисунке 5. Если поля связи таблицы имеют нестандартные имена, то они указываются в *«joins».* Запросы к РСУБД, относящиеся к этим конечным точкам, находятся в листингах 51–52.

Листинг 50 — Пример указания связей между таблицами.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": ["ref",table1\_ref]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetTable1RefTable2":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`       `"endpointGetTable2RefTable1":{</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 51 — Запроса к РСУБД, относящейся к листингу 50. Для *«endpointGetTable1RefTable2».*

|<p>SELECT \*FROM table2</p><p>WHERE table1\_ref IN (SELECT ref FROM table1);</p>|
| :- |

Листинг 52 — Запроса к РСУБД, относящейся к листингу 50. Для *«endpointGetTable2RefTable1»*

|<p>SELECT \*FROM table1</p><p>WHERE ref IN (SELECT table1\_ref FROM table2);</p>|
| :- |

Тип связи не важен для всех типов запросов, кроме *POST*. В случае связи многие к одному, знак связи: «>» для *«POST»*, следует указывать в последней таблице в конечной точке.

**Многие к Одному**

Схема базы данных, к которой относится следующий запрос, изображена на схеме рисунка 4. Тип связи многие к одному можно указывать и в *«joins»*, однако, в этом случае нельзя дать полям связи иные имена, кроме тех, что будут даны по умолчанию (листинг 53). Запрос к РСУБД, находятся в листингах 54–55.

Листинг 53 — Пример указания связей между таблицами.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": ["<"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetTable1ManyToOneTable2":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`       `"endpointGetTable2ManyToOneTable1":{</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 54 — Запроса к РСУБД, относящейся к листингу 53. Для *«endpointGetTable1ManyToOneTable2».*

|<p>SELECT \* FROM table2</p><p>WHERE TABLE1\_ID IN (</p><p>SELECT ID FROM table1);</p>|
| :- |

` `Листинг 55 — Запроса к РСУБД, относящейся к листингу 53. Для *«endpointGetTable2ManyToOneTable1».*

|<p>SELECT \* FROM table1</p><p>WHERE ID IN (</p><p>SELECT TABLE1\_ID FROM table2);</p>|
| :- |





![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.004.png)![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.005.png)![Изображение выглядит как текст, снимок экрана, Шрифт, число

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.006.png)![Изображение выглядит как текст, снимок экрана, Шрифт, число

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.007.jpeg)



**Один ко многим**

Схема базы данных, к которой относится запрос из листинга 56, изображена на схеме рисунка 4. Тип связи один ко многим можно указывать и в *«joins».* Однако, в этом случае нельзя дать полям связи иные имена, кроме тех, что будут даны по умолчанию. Запрос к РСУБД (листинг 57–58).

Листинг 56 — Пример указания связей между таблицами.

|<p>{</p><p>"Table1Group": {</p><p>"pseudonyms": {</p><p>"joins": {</p><p>"table2:table1": [">"]</p><p>}</p><p>},</p><p>"http":{      "endpointGetTable1OneToManyTable2": {</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`       `"endpointGetTable2OneToManyTable1": {</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

` `Листинг 57 — Запроса к РСУБД, относящейся к листингу 56 для *«endpointGetTable1OneToManyTable2».*

|<p>SELECT \* FROM table2</p><p>WHERE TABLE1\_ID IN (</p><p>SELECT ID FROM table1);</p>|
| :- |

Листинг 58 — Запроса к РСУБД, относящейся к листингу 56 для *«endpointGetTable2OneToManyTable1».*

|<p>SELECT \* FROM table1</p><p>WHERE ID IN (</p><p>SELECT TABLE1\_ID FROM table2);</p>|
| :- |

**Многие ко многим**

Схема базы данных, к которой относится запрос из листинга 59, изображена на рисунке 19. Запрос к РСУБД (листинг 60–61).

Листинг 59 — Пример указания связей между таблицами.

|<p>{</p><p>"Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": ["table2\_has\_table1"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetTable1ManyToManyTable2":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2" },</p><p>`        `"endpointGetTable2ManyToManyTable1":{</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1" }</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |





![Изображение выглядит как текст, снимок экрана, Шрифт, число

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.008.jpeg)![Изображение выглядит как текст, снимок экрана, Шрифт, число

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.009.jpeg)![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.010.png)![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.011.png)        

Листинг 60 — Запроса к РСУБД, относящейся к листингу 59 для *«endpointGetTable1ManyToManyTable2».*

|<p>SELECT \*FROM table2 WHERE ID IN (</p><p>SELECT TABLE2\_ID FROM table2\_has\_table1</p><p>WHERE TABLE1\_ID IN (SELECT ID FROM table1));</p>|
| :- |

` `Листинг 61 — Запроса к РСУБД, относящейся к листингу 59 для *«endpointGetTable2ManyToManyTable1».*

|<p>SELECT \*FROM table1 WHERE ID IN (</p><p>SELECT TABLE1\_ID FROM table2\_has\_table1</p><p>WHERE TABLE2\_ID IN (SELECT ID FROM table2));</p>|
| :- |

Схема базы данных, к которой относится запрос из листинга 62, изображена на рисунке 6. Запрос к РСУБД (листинг 63–64).

Листинг 62 — Пример указания связей между таблицами.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": ["table2\_has\_table1"],</p><p>`        `"table2:table1\_has\_table2": ["ref","table2\_ref"],</p><p>`        `"table1:table1\_has\_table2": ["ref","table1\_ref"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`        `"endpointGetTable1ManyToManyTable2Ref":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`        `"endpointGetTable2ManyToManyTable1Ref":{</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 63 — Запроса к РСУБД, относящейся к листингу 62. Для *«endpointGetTable1ManyToManyTable2Ref».*

|<p>SELECT \*FROM table2 WHERE ref IN (</p><p>SELECT TABLE2\_ref FROM table2\_has\_table1</p><p>WHERE TABLE1\_ref IN (SELECT ref FROM table1));</p>|
| :- |

` `Листинг 64 — Запроса к РСУБД, относящейся к листингу 62. Для *«endpointGetTable2ManyToManyTable1Ref».*

|<p>SELECT \*FROM table1 WHERE ref IN (</p><p>SELECT TABLE1\_ref FROM table2\_has\_table1</p><p>WHERE TABLE2\_ref IN (SELECT ref FROM table2));</p>|
| :- |

**Связь по умолчанию**

Если связь между таблицами по умолчанию, или одно из полей по умолчанию, то следует обозначить лишь факт ее наличия «:».

Листинг 65 — Пример указания связей между таблицами.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": [":",":"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetRef":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

**Дейкстра**

Листинг 66 — Поиск связей.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"table1:table2": [">"],</p><p>`        `"table2:table3": ["<"],</p><p>`        `"table3:table4": ["table3\_has\_table4"],</p><p>`        `"table4:table5": [">"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetDeicstra":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table5"</p><p>`      `},</p><p>`      `"endpointGetNoDeicstra":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2/table3/table4/table5"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Для удобства написания конечных точек был реализован алгоритм Дейкстры (описание находится в источнике: [20]). В случае указания и наличия связи в «*joins»* через различные “пропущенные таблицы” будет запущен алгоритм Дейкстры. В листинг 66 запрос к РСУБД в *«endpointGetDeicstra»* c *«endpointGetNoDeicstra»* будет одинаковым.

**Односторонняя связь**

По умолчанию связь между таблицами является двусторонней. Однако, если необходимо установить одностороннюю связь или связь, когда таблицы связаны через разные поля в разных направлениях, следует использовать символ «!» перед связью (листинг 67). Запрос к РСУБД для листинга 67 находится в листингах 68–69.

Листинг 67 — Пример указания связей между таблицами в одну сторону.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"joins": {</p><p>`        `"!table1:table2": ["ref", "table1\_ref"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`       `"endpointGetOneRef":{</p><p>`        `"type":"get",</p><p>`        `"request":"table1/table2"</p><p>`      `},</p><p>`      `"endpointGetDefaultRef":{</p><p>`        `"type":"get",</p><p>`        `"request":"table2/table1"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 68 — Запроса к РСУБД, относящейся к листингу 67. Для *«endpointGetOneRef».*

|<p>SELECT \*FROM table2 </p><p>WHERE table1\_ref IN (</p><p>SELECT ref FROM table1);</p>|
| :- |

` `Листинг 69 — *Запроса к РСУБД, относящейся к листингу 67. Для «endpointGetDafaultRef».*

|<p>SELECT \*FROM table1 </p><p>WHERE table2\_id IN ( </p><p>SELECT id FROM table2);</p>|
| :- |

**Связь через псевдонимы таблиц**

Схема базы данных, к которой относится это запрос, изображена на рисунке 8. Связь можно указывать ни только через реальные имена таблицы, но и через eё псевдонимы, задаваемые в параметрах [*«tables](#_heading=h.1baon6m)*»* (листинг 70) . Запрос к РСУБД для листинга 70 находится в листинге 71.

Однако стоит учитывать, что поиск связей или пропущенных таблиц будет вестись сначала через псевдонимы или псевдонима и реального имени, используемые в конечной точке. В случае неуспеха все этапы поиска связей пойдут через реальные имена таблицы.


![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.012.png)![Изображение выглядит как текст, снимок экрана, Шрифт, число

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.013.jpeg)

Листинг 70 — Пример указания связей между таблицами через их псевдонимы.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"tables": {</p><p>`        `"table1": ["parent","child"]</p><p>`      `},</p><p>`      `"joins":{</p><p>`        `"parent:child":["parent\_has\_child"],</p><p>`        `"parent:parent\_has\_child":["id","parent\_id"],</p><p>`        `"child:parent\_has\_child":["id","child\_id"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`      `"endpointGetPseudonymsRef":{</p><p>`        `"type":"get",</p><p>`        `"request":"parent/child"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 71 — Запроса к РСУБД, относящейся к листингу 70. Для *«endpointGetPseudonymsRef».*

|<p>SELECT \*FROM table1 as “child” </p><p>WHERE ID IN (</p><p>SELECT chield\_id FROM </p><p>parent\_has\_chield</p><p>WHERE parent\_id IN (</p><p>SELECT ID FROM table1 as “parent”));</p>|
| :- |
###### ***«tables»***
При использовании восклицательного знака перед псевдонимом, для таблицы все поля, в случае если они заданы по умолчанию, генерируются в формате <Псевдоним>\_id. Таким образом вместо «*Table1\_id»* сгенерировалось *«Table\_id»;*

Листинг 72 — Пример использования псевдонимов для таблицы.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"tables": {</p><p>`        `"table1": ["t1","!Table"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`      `"endpointGet":{</p><p>`        `"type":"get",</p><p>`        `"request":"t1/Table2"</p><p>`      `},</p><p>`       `"endpointGetRef":{</p><p>`        `"type":"get",</p><p>`        `"request":"Table/Table2"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

` `Листинг 73 — Запроса к РСУБД, относящейся к листингу 72*.*  Для *«endpointGet».*

|<p>SELECT \* FROM TABLE2</p><p>WHERE Table1\_id IN (</p><p>SELECT ID FROM Table1 as 'T1');</p>|
| :- |

Листинг 74 — Запроса к РСУБД, относящейся к листингу 72. Для *«endpointGetRef».*

|<p>SELECT \* FROM TABLE2</p><p>WHERE Table\_id IN </p><p>(SELECT ID FROM Table1 as 'Table');</p>|
| :- |
###### ***«fields»***
Листинг 75 — Пример использования псевдонимов для полей.

|<p>{</p><p>"Table1Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"fields": {</p><p>`        `"Field1": ["F1","someF"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`      `"endpointGet":{</p><p>`        `"get":"someF",</p><p>`        `"request":"Table1/{F1-s}"</p><p>`      `},</p><p>`       `"endpointPost":{</p><p>`        `"post":"someF->F1",</p><p>`        `"request":"Table1"</p><p>`      `}</p><p>`    `}</p><p>`      `}</p><p>}</p>|
| :- |

Листинг 76 — *Запроса к РСУБД, относящейся к листингу 75. Для «endpointGet».*

|<p>SELECT FIELD1 as 'someF' </p><p>FROM TABLE1 WHERE FIELD1=?;</p>|
| :- |

` `Листинг *77**— *Запроса к РСУБД, относящейся к листингу 75. Для «endpointPost».*

|<p>INSERT INTO TABLE1 (Field1) VALUES(?) </p><p>RETURNING Field1 as 'F1';</p>|
| :- |

При этом *«Field1»* передается в теле запроса как *«someF».* Псевдонимы для полей можно использовать в фильтрах, теле запроса, в конечной точке. Однако нельзя использовать в *«joins».* В них можно указывать только настоящие названия полей.
##### *«entity»*
Код, приведенный в листингах 78 и 79 является равноценным.

Листинг 78 — Пример использования *«entity».*

|<p>` `{</p><p>"Table5Group":{</p><p>`    `"pseudonyms": {</p><p>`      `"entity": {</p><p>`        `"entity1": ["F1-s=str","F2-i=6","F3-i=10"],</p><p>`        `"entity2": ["F4-i=13"]</p><p>`      `}</p><p>`    `},</p><p>`    `"http":{</p><p>`      `"endpoint":{</p><p>`        `"patch":"entity1|entuty2|F5-i=19->entity2|id",</p><p>`        `"request":"Table5/{F5-s}"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

Листинг 79 — Пример отсутствия *«entity».*

|<p>{</p><p>"Table5Group":{</p><p>`  `"http":{</p><p>`      `"endpoint":{</p><p>`        `"patch":"F1-s=str|F2-i=6|F3-i=10|F4-i=13|F5-i=19->F4|id",</p><p>`        `"request":"Table5/{F5-s}"</p><p>`      `}</p><p>`    `}</p><p>`  `}</p><p>}</p>|
| :- |

*«entity»-* можно использовать в качестве тела запроса для: *POST*, *PATCH*, *PUT* и возвращаемых значений для *POST*, *PATCH,* *PUT,* *GET*. В качестве дополнительных параметров (*«limit|sort|fields»*) для *GET*, *«entity»* использовать нельзя.
1. #### **Фильтры**
Фильтры могут быть либо на уровне контроллера, либо на уровне конечной точки. При одном и том же имени, фильтр, находящийся в конечной точке, будет иметь преимущество. Код функции фильтра будет сгенерирован в том случае, если фильтр будет присутствовать в *«request».*

Фильтр имеет формат (листинг 80).

Листинг 80 — Формат фильтра.

|"FilterName:type=Value":"someInfo"|
| :- |

*«=Value»* - не обязательно и в случае его присутствия оно будет в *«request»* вместо *«[filterName]».*

Листинг 81 — Пример использования фильтра.

|<p>"GroupEndpoint": {</p><p>`    `"filters": {</p><p>`      `"filterCall:call": "org.example.filter#CallClass#make1",</p><p>`      `"filterAnd:and": "field1|field2",</p><p>`      `"filterOr:or": "field1|field2",</p><p>`      `"filterNotAnd:!and": "field1|field2",</p><p>`      `"filterNotOr:!or": "field1|field2"</p><p>`    `},</p><p>`    `"http": {</p><p>`      `"Endpoint": {</p><p>`        `"request": "table/[filterCall]|[filterAnd]/[filterOrOne]",</p><p>`        `"filters": {</p><p>`            `"filterCall:call": "org.example.filter#CallClass#make2",</p><p>`            `"filterAndOne:1-and": "field1|field2",</p><p>`            `"filterOrOne:1-or": "field1|field2",</p><p>`            `"filterNotAndOne:!1-and": "field1|field2",</p><p>`            `"filterNotOrOne:!1-or": "field1|field2"</p><p>`        `}</p><p>`      `}</p><p>`    `}</p><p>}</p>|
| :- |
###### **Фильтр «:call»**
Листинг 82 — Пример фильтра *«:call».*

|<p>"filters":{  </p><p>"filterCall:call":"org.example.filter#CallClass#make"</p><p>}</p>|
| :- |

В нужном месте запроса будет вызвана статическая функция у класса, прототип которой находится в листинге 83.

Листинг 83 — Прототип функции для фильтра из листинга 82.

|<p>package org.example.filter;</p><p>public record CallClass() {</p><p>public static Condition make(</p><p>MultiValueMap<String,String> filterCall,</p><p>String table){</p><p>`  `return //</p><p>`   `}</p><p>}</p>|
| :- |

В запросе параметр *«MultiValueMap<String, String> filterCall»* будет иметь имя фильтра. В данном случае *«filterCall».*
###### **Фильтр «:and»**
Листинг 84 — Пример фильтра *«:and».*

|<p>"filters": {</p><p>`     `"filterAnd:and": "like\_field1-s|field2"</p><p>}</p>|
| :- |

Для листинга 84 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 85.

Листинг 85 — Прототип функции для фильтра из листинга 84.

|<p>public Condition EndpointFilterAndOfEndpoint(</p><p>MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if (returnFields.containsKey("like\_field1")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .like(DSL.val(returnFields.getFirst("like\_field1"), </p><p>`      `String.class)));</p><p>`    `}</p><p>`    `if (returnFields.containsKey("field2")) {</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(returnFields.getFirst("field2"),</p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `return conditions.stream().reduce(Condition::and)</p><p>        .ofNullable(defaultCondition).get();</p><p>`  `}</p>|
| :- |
###### **Фильтр *«:or»***
Листинг 86 — Пример фильтра *«:or».*

|<p>` `"filters": {</p><p>`      `"filterOr:or": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 86 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 87.

Листинг 87 — Прототип функции для фильтра из листинга 86.

|<p>public Condition EndpointFilterOrOfEndpoint(</p><p>` `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if (returnFields.containsKey("field1")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .eq(DSL.val(returnFields.getFirst("field1"),</p><p>`     `String.class)));</p><p>`    `}</p><p>`    `if (returnFields.containsKey("field2")) {</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(returnFields.getFirst("field2"),</p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `return conditions.stream().reduce(Condition::or)</p><p>        .ofNullable(defaultCondition).get();</p><p>`  `}</p>|
| :- |
###### **Фильтр «:!and»**
Листинг 88 — Пример фильтра *«:!and».*

|<p>"filters": {</p><p>`      `"filterNotAnd:!and": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 88 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 89.

Листинг 89 — Прототип функции для фильтра из листинга 88.

|<p>public Condition EndpointFilterNotAndOfEndpoint(</p><p>` `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if (returnFields.containsKey("field1")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .eq(DSL.val(returnFields.getFirst("field1"), String.class)));</p><p>`    `}</p><p>`    `if (returnFields.containsKey("field2")) {</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(returnFields.getFirst("field2"),  Long.class)));</p><p>`    `}</p><p>`    `if(conditions.isEmpty()) {</p><p>`      `return defaultCondition;</p><p>`    `}</p><p>`    `return DSL.not(conditions.stream().reduce(Condition::and)</p><p>        .ofNullable(defaultCondition).get()); </p><p>}</p>|
| :- |
###### **Фильтр *«:!or»***
Листинг 90 — Пример фильтра *«:!or».*

|<p>"filters": {</p><p>`      `"filterOr:!or": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 90 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 91.

Листинг 91 — Прототип функции для фильтра из листинга 90.

|<p>public Condition EndpointFilterNotOrOfEndpoint(</p><p>` `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if (returnFields.containsKey("field1")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .eq(DSL.val(returnFields.getFirst("field1"), </p><p>`     `String.class)));</p><p>`    `}</p><p>`    `if (returnFields.containsKey("field2")) {</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(returnFields.getFirst("field2"), </p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `if(conditions.isEmpty()) {</p><p>`      `return defaultCondition;</p><p>`    `}</p><p>`    `return DSL.not(conditions.stream().reduce(Condition::or)</p><p>        .ofNullable(defaultCondition).get());</p><p>`  `}</p>|
| :- |
###### **Фильтр «:1-and»**
Листинг 92 — Пример фильтра *«:1-and».*

|<p>"filters": {</p><p>`      `"filterAndOne:1-and": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 92 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 93.

Листинг 93 — Прототип функции для фильтра из листинга 92.

|<p>public Condition EndpointFilterAndOneOfEndpoint(</p><p>` `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if(returnFields.containsKey("filterAndOne")) {</p><p>`    `conditions.add(DSL.field("field1")</p><p>    .eq(DSL.val(</p><p>`    `returnFields.getFirst("filterAndOne"),</p><p>`    `Long.class)));</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterAndOne"), </p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `return conditions.stream().reduce(Condition::and)</p><p>        .ofNullable(defaultCondition).get();</p><p>`  `}</p>|
| :- |
###### **Фильтр «:1-or»**
Листинг 94 — Пример фильтра *«:1-or».*

|<p>"filters": {</p><p>`      `"filterOrOne:1-or": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 94 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 95.

Листинг 95 — Прототип функции для фильтра из листинга 94.

|<p>`  `public Condition EndpointFilterOrOneOfEndpoint(</p><p>`  `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if(returnFields.containsKey("filterOrOne")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterOrOne"), </p><p>`      `Long.class)));</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterOrOne"), </p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `return conditions.stream().reduce(Condition::or)</p><p>        .ofNullable(defaultCondition).get();</p><p>`  `}</p>|
| :- |
###### **Фильтр «:!1-and»**
Листинг 96 — Пример фильтра *«:!1-and».*

|<p>"filters": {</p><p>`      `"filterNotAndOne:!1-and": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 96 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 97.

Листинг 97 — Прототип функции для фильтра из листинга 96.

|<p>`   `public Condition EndpointFilterNotAndOneOfEndpoint(</p><p>`   `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if(returnFields.containsKey("filterNotAndOne")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterNotAndOne"), </p><p>`      `Long.class)));</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterNotAndOne"), </p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `if(conditions.isEmpty()) {</p><p>`      `return defaultCondition;</p><p>`    `}</p><p>`    `return DSL.not(conditions.stream().reduce(Condition::and)</p><p>        .ofNullable(defaultCondition).get());</p><p>`  `}</p>|
| :- |
###### **Фильтр «:!1-or»**
Листинг 98 — Пример фильтра *«:!1-or».*

|<p>"filters": {</p><p>`      `"filterNotOrOne:!1-or": "field1|field2"</p><p>}</p>|
| :- |

Для листинга 98 будет сгенерирована и вызвана в нужном месте функция, которая находится в листинге 99.

Листинг 99 — Прототип функции для фильтра из листинга 98.

|<p>`   `public Condition EndpointFilterNotOrOneOfEndpoint(</p><p>`  `MultiValueMap<String, String> returnFields,</p><p>`      `String table, Condition defaultCondition) {</p><p>`    `List<Condition> conditions=new ArrayList<>();</p><p>`    `if(returnFields.containsKey("filterNotOrOne")) {</p><p>`      `conditions.add(DSL.field("field1")</p><p>     .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterNotOrOne"), </p><p>`      `Long.class)));</p><p>`      `conditions.add(DSL.field("field2")</p><p>      .eq(DSL.val(</p><p>`      `returnFields.getFirst("filterNotOrOne"), </p><p>`      `Long.class)));</p><p>`    `}</p><p>`    `if(conditions.isEmpty()) {</p><p>`      `return defaultCondition;</p><p>`    `}</p><p>`    `return DSL.not(conditions.stream().reduce(Condition::or)</p><p>        .ofNullable(defaultCondition).get());</p><p>`  `}</p>|
| :- |
1. #### ` `**Пути к JSON файлу**
Для удобного формата файла JSON, была реализована подгрузка информации из других файлов, используемых в таких местах, как группа конечных точек поле *«filters»,* *«http», «pseudonyms2*. Благодаря этому один большой файл (листинг 100) можно разделить на файлы меньшего размера (листинг 101–108).

Листинг 100 — Пример не разделенного файла.

|<p>{</p><p>`    `"Group1": {</p><p>`      `"pseudonyms": {</p><p>`        `"tables": {</p><p>`          `"t1": ["table1"]</p><p>`        `},</p><p>`        `"fields": {</p><p>`          `"ref": ["id"]</p><p>` `}</p><p>`      `},</p><p>`      `"filters": {</p><p>`        `"f1:call": "some#ClassFilter#callFunc"</p><p>`      `},</p><p>`      `"http": {</p><p>`        `"endpoint1": {</p><p>`          `"request": "table1/{ref}/[f1]"</p><p>`        `}</p><p>`      `}</p><p>`    `},</p><p>`    `"Group2": {</p><p>`      `"pseudonyms": {</p><p>`        `"tables": {</p><p>`          `"t1": ["table1"]</p><p>`        `},</p><p>`        `"fields": {</p><p>`          `"ref": ["id"]</p><p>`        `}</p><p>`      `},</p><p>`      `"filters": {</p><p>`        `"f1:call": "some#ClassFilter#callFunc"</p><p>`      `},</p><p>`      `"http": {</p><p>`        `"endpoint1": {</p><p>`          `"request": "table2/[f1]",</p><p>`          `"filters": {</p><p>`            `"f1:call": "some#ClassFilter#callFunc2"</p><p>`          `}</p><p>`        `}</p><p>`      `}</p><p>`    `}</p><p>}</p>|
| :- |

Листинг 101 — «*MainFile.json».*

|<p>{</p><p>`    `"Group1": "Group1.json",</p><p>`    `"Group2": "Group2.json"</p><p>}</p>|
| :- |

Листинг 102 — Group1.json.

|<p>{</p><p>`      `"pseudonyms": "Pseudonyms.json",</p><p>`      `"filters": "filtersInGroup.json",</p><p>`      `"http":  "httpOfGroup1.json"</p><p>` `}</p>|
| :- |

Листинг 103 — *«Group2.json».*

|<p>{</p><p>`      `"pseudonyms":"Pseudonyms.json",</p><p>`      `"filters":"filtersInGroup.json",</p><p>`      `"http":"httpOfGroup2.json"</p><p>}</p>|
| :- |

` `Листинг 104 — *«Pseudonyms.json».*

|<p>{</p><p>`    `"tables": {</p><p>`    `"t1": ["table1"]</p><p>`    `},</p><p>`    `"fields": {</p><p>`     `"ref": ["id"]</p><p>`    `}</p><p>} </p>|
| :- |

Листинг 105 — *«httpOfGroup1.json».*

|<p>{</p><p>`    `"endpoint1": {</p><p>`    `"request": "table1/{ref}/[f1]"</p><p>`    `}</p><p>} </p>|
| :- |

Листинг 106 — *«httpOfGroup2.json».*

|<p>{</p><p>`    `"endpoint1": {</p><p>`    `"request": "table2/[f1]",</p><p>`    `"filters":filtersInEndpontOfGroup2.json</p><p>`    `}</p><p>}</p>|
| :- |

Листинг 107 — *«filtersInGroup.json».*

|<p>{</p><p>`    `"f1:call": "some#ClassFilter#callFunc"</p><p>}</p>|
| :- |

Листинг 108 — *«filtersInEndpontOfGroup2.json».*

|<p>{</p><p>`    `"f1:call": "some#ClassFilter#callFunc2"  </p><p>} </p>|
| :- |

Необходимо отметить, что в приведенном примере не указаны полные пути к файлам. Однако в реальной практике они должны быть указаны полными.
1. #### ` `**Файл: *«application.properties»***
Помимо стандартных параметров *Spring-Boot* приложения файла *«application.properties»* (листинг 109).

Листинг 109 — *«application.properties».* Cтандартные настройки.

|<p>spring.application.name=RestApiApplication </p><p>server.port=8080</p><p>spring.datasource.url=jdbc:postgresąl://localLost:5433/postgres spring.datasource.username=postures </p><p>spring.datasource.password=password</p><p>spring.jooq.sql-dialect=POSTGRES</p>|
| :- |

Были добавлены дополнительные настройки, используемые в сгенерированном коде (листинг 110).

Листинг 110 — *«application.properties».* Дополнительные настройки.

|<p>restApi.openApi.title=RestApi </p><p>restApi.openApi.description=Manager </p><p>restApi.openApi.version=1.0 </p><p>restApi.showSql=true</p>|
| :- |

Параметр из листинга 111 отвечает за показ кода, отправляемого к базе данных. 

Листинг 111 — Показ SQL.

|restApi.showSql|
| :- |

Параметры из листинга 112 используются при конфигурации *Swagger.*

Листинг 112 — Конфигурации *Swagger.*

|<p>restApi.openApi.title </p><p>restApi.openApi.description </p><p>restApi.openApi.version</p>|
| :- |
#### **13. Configuration**
Код сгенерированного класса конфигурации находится в листинге 113.

Листинг 113 —  «*Configuration».*

|<p>@Configuration</p><p>public class ConfigRest {</p><p>`  `@Bean</p><p>`  `public OpenAPI usersMicroserviceOpenAPI </p><p>` `(@Value("${restApi.openApi.title:}") String title,</p><p>`  `@Value("${restApi.openApi.description:}") String description,</p><p>`  `@Value("${restApi.openApi.version:}") String version) {</p><p>`    `return new OpenAPI()</p><p>    .info(new Info()</p><p>    .title(title)</p><p>    .description(description)</p><p>    .version(version));</p><p>`  `}</p><p>}</p>|
| :- |
##### ***«Operation»***
Для более красивого обозначения в конечных точках для каждого метода в контроллере добавляется аннотация (листинг 114).

Листинг 114 — Operation.

|@Operation(summary="")|
| :- |

Была реализована возможность добавления таких параметров как *«tags»* и «summary».
##### ***«tags» и «summary»***
Параметры «tags» и «summary» (листинг 115–116).

Листинг 115 — «tags» и «summary».

|<p>"EndpointName7": {</p><p>`    `"types":{</p><p>`        `"get":{</p><p>`        `"summary":"Oписание",</p><p>`        `"tags":"tag1|tag2"</p><p>`        `} </p><p>`    `},</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

Листинг 116 —  «tags» и «summary».

|<p>"EndpointName7": {</p><p>`    `"type":"get",</p><p>`    `"summary":"Oписание",</p><p>`    `"tags":"tag1|tag2",</p><p>`    `"request": "endpoint"</p><p>}</p>|
| :- |

Сгенерированный код для листинга 115 и 116 будет представлен в листинге 117.

Листинг 117 — Код для листинга 115 или 116. 

|@Operation (summary="Oписание", tags={"tag1","tag2"})|
| :- |

В случае следующего формата данных для листинга 118 невозможно указать *«summary»,* но возможно указать в *«tags».*

Листинг 118 — *«tags”.*

|<p>"EndpointName7": {</p><p>`    `"get":"",</p><p>`    `"delete":"",</p><p>`    `"tags":"tag1|tag2",</p><p>`    `"request": "endpoint"</p><p>}* </p>|
| :- |

Сгенерированный код для листинга 115–116 и 118 представлен в листинг 119.

Листинг 119 — Код для листинга 118.

|@Operation(summary="", tags={"tag1","tag2"})|
| :- |
#### **14. Компиляция**
` `В случае успеха компиляции, будет выведена информация о сгенерированных файлах (рисунок 10).

В случае некорректного JSON файла, будет выведена информация о месте предполагаемой ошибки (рисунок 9).



![](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.014.png)![Изображение выглядит как текст, снимок экрана, Шрифт

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.015.jpeg)

![Изображение выглядит как текст, снимок экрана, Шрифт, дизайн

Автоматически созданное описание](Aspose.Words.630e6765-dcba-4fc4-a925-2a3bf6c2ea19.016.jpeg)

Рисунок 10 — Компиляция прошла успешно.


