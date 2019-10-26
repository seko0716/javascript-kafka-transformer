# Kafka script transformers

The project is a sample and example of a kafka-connect transformers. 

The kafka transformer for applying custom scripts to kafka record (key and value). kafka scheme not using.

* JavaScript

JavaScript language is the only of supported languages, which have restrictions to call java API. JavaScript can't execute code System.exit() File.delete and others.

It is recommended to use JavaScript language, because it is optimal by performance and security.

# Configuration

## Examples:

### java script
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "javascript",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.key.script": "function keyTransform(source){ source.qweqweq = 12312312; return source;}",
  "transforms.ScriptEngineTransformer.value.script": "function valueTransform(source){ source.qweqweq = 12312312; return source;}"
}
```

# JMH test results

###### JMH version: 1.21
###### VM version: JDK 1.8.0_222, OpenJDK 64-Bit Server VM, 25.222-b05
###### VM invoker: /usr/lib/jvm/java-8-openjdk/jre/bin/java
###### VM options: -Xms2G -Xmx2G
###### Warmup: 5 iterations, 10 s each
###### Measurement: 5 iterations, 10 s each
###### Timeout: 10 min per iteration
###### Threads: 1 thread, will synchronize iterations
###### Benchmark mode: Average time, time/op
###### Benchmark: seko.kafka.connect.transformer.jmh.tests.TransformersTest.groovyTransformer
###### Parameters: (N = 10000000)


Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.jsTransformer":
  - 173.391 ±(99.9%) 5.602 ns/op [Average]
  - (min, avg, max) = (169.248, 173.391, 177.589), stdev = 3.705
  - CI (99.9%): [167.788, 178.993] (assumes normal distribution)

##### Run complete. Total time: 00:10:06

### REMEMBER: 
The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.



|Benchmark                           |      (N)  | Mode  | Cnt |       Score |       Error | Units |
| ---------------------------------- | --------- | ----- | --- | ----------- | ----------- | ----- |
|TransformersTest.javaNativeTransformer    |  10000000 | avgt  | 10  |      51.584 |±     0.470  | ns/op |
|TransformersTest.jsTransformer      |  10000000 | avgt  | 10  |     173.391 |±     5.602  | ns/op |

