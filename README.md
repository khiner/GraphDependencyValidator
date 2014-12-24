### Graph Dependency Validator

Coding challenge submission.  Here is the description:

Given the following dependency graph and a stream of events, implement a Java solution to verify that all dependencies for a row have been satisfied. 

Implementation should be able to handle error scenarios and should be optimized for efficiency.

Helper libraries can be used if needed.  Feel free to provide unit test case(s) as needed. Documentation (javadoc) is not necessary.

Dependency  graph:

```
A   ->  {}
B   ->  {A}
C   ->  {A}
D   ->  {B,C}
E   ->  {}
F   ->  {B}
```

Sample  stream  of  events:

```
{"type":"A",    "id":1, "name":"apple"}
{"type":"A",    "id":2, "name":"banana"}
{"type":"B",    "id":1, "parent":[{ "type":"A", "id":1  }],"name":"volvo"}
{"type":"B",    "id":2, "parent":[{ "type":"A", "id":2  }], "name":"audi"}
{"type":"C",    "id":1, "parent":[{ "type":"A", "id":1  }],"name":"soccer"}
{"type":"C",    "id":2, "parent":[{ "type":"A", "id":2  }],"name":"football"}
{"type":"D",    "id":1, "parent":[{ "type":"B", "id":2  },  {"type":"C","id":1}], "name":"squirrels"}
{"type":"D",    "id":2, "parent":[{ "type":"B", "id":1  },  {"type":"C","id":2}], "name":"chipmunk"}
{"type":"E",    "id":1, "name":"plane"}
{"type":"E",    "id":2, "name":"train"}
{"type":"F",    "id":8, "parent":[{ "type":"B", "id":1}], "name":"squid"}
```

## Build/run instructions

To run the main application, do one of the two:

1) Open the project in IDEA, right-click the main class (src/com/kh/Main.java) and select 'Run', or...

2) In a terminal navigate to the project root and run `./gradlew build run`
