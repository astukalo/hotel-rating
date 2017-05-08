# How to run
1. run `gradlew clean build` in this directory
2. cd `build/distributions/`
3. copy `hotel-rating-1.0-SNAPSHOT.tar` to any directory
4. untar the archive there 
5. cd to directory 
6. run `bin/hotel-rating your_topic /path/to/reviews`
7. app will print result and (debug) statistics about the processed reviews

# About
Used dependency analysis [https://nlp.stanford.edu/software/dependencies_manual.pdf] 
to process reviews and to find out characteristics for a given topic.
Based on those characteristics, intensifiers and provided classifiers 
sentiment score is calculated for a review and for a hotel.