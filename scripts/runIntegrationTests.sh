echo "Start Integration Tests..."
cd ..

echo "Running Maven test..."
mvn test -Dtest="**/integration/**/*Test,**/integration/**/*Tests" -DfailIfNoTests=false

cd scripts || exit
