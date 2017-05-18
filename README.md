# JavaBenchmark
Java benchmark is a reallly easy to use lib for microbenchmarking.
Every method that is being benchmarked is run milion times, and the resulting time is average of that.
## Example usage

```java
class MyBenchmark implements Benchmark {
    public void setUp(){
        /// methods that will be invoked once, before all benchmarks happen   
    }

    public void beforeBenchmark(){
        /// methods that will be invoked before every single method run
    }

    public void afterBenchmark(){
        /// methods that will be invoked after every single method run
    }
    
    @Test
    public void pieceOfCodeThatIWantToBenchmark(){
        someMethod();
    }

    @Test
    @Args(arguments={10,100,1000})
    public void pieceOfCodeWithArguments(int arg){
        someMethodWithArgs(arg);
    }
}
```
Implementing ```setUp()```, ```beforeBenchmark()```, and ```afterBenchmark()``` is not mandatory.
Also you have to create:
```java
class Main{
    public static void main(String[] args){
        BenchmarkRunner.run();
    }
}
```
## Maven
add to your pom.xml file
```xml
<repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
</repository>
```
below after ``` <dependencies> ``` tag add:
```xml
<dependency>
  <groupId>com.github.Wiewiogr</groupId>
  <artifactId>JavaBenchmark</artifactId>
  <version>[latest commit hash]</version>
</dependency>
```
