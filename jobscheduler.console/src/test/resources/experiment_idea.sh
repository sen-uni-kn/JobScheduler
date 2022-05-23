
java -Xmx200g -cp ../../test-1.0.0-jar-with-dependencies.jar Job_Console \
-destiny ../results.txt -prefix 1_ses -store Result1 -id job1\
Job_Hello -input1 Frida

java -Xmx200g -cp ../../test-1.0.0-jar-with-dependencies.jar Job_Console \
-destiny ../results.txt -prefix 1_ses -store Result1 -id job2\
Job_Bye -input1 {$job1/result1}

java -Xmx200g -cp ../../test-1.0.0-jar-with-dependencies.jar Job_Console \
-destiny ../results.txt -pref 2_ses -store Result1 -id job1 \
Job_Hello -input1 Boris

java -Xmx200g -cp ../../test-1.0.0-jar-with-dependencies.jar Job_Console \
-destiny ../results.txt -prefix 2_ses -store Result1 -id job2\
Job_Bye -input1 {$job1/result1}
