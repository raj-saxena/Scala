# WebPageAnalyzer

## Requirements
* [Sbt](http://www.scala-sbt.org/download.html) is installed

* Go inside project directory
    - run tests with `sbt test`
    - run with `sbt run`
    - Go to "http://localhost:9000/analyze" on the browser to interact with the app.
    
* Assumptions
    - User will enter the right url along with schema Eg: http://www.example.com, https://github.com
    - Links that have complete same domain/sub-domain are only considered for internal/external categorization. 
    For eg: www.example.com and www.example.com/link1 are considered internal
   but abc.example.com and xyz.example.com are considered as external to eachother.
    
* Thoughts on performance
    - Used `Http HEAD` instead of `GET` as it's faster.
    - As the process takes long to visit each link in the page, it is important to have a cache by link url.
    - Used default `ehcache` for it but don't think it's working as should, need to revisit...
    
* Known issues
    - Scala doesn't supports JDK 9 completely now - https://github.com/scala/scala-dev/issues/139 . Run with JDk 8