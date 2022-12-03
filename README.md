# RevisionSchedulerService
* Users can able to register their learnings and have a revision pattern like remind me for first revision after 5 days,second revision after 30 days 3'rd one after 120 days , etc
* Based on revision pattern users will get reminder notificatons(email for now)
* Tech: spring boot + ReactJs + aws

####Development setup
#####Backend

  mvn spring-boot:run  to start project

#####Frontend

  npm start 
###local mysql db  commands
mysql -u tejaaa -p ; pass is mysqlpass
show databases;
use dev;
show tables;
show create table user_learning_item; # shows ddl of a table with constraints.

###Registration/Signup
.when user signup save token in db , send mail to user with that token and have user state as disabled
.when user clicks on HTTP::Get::domain.com/userid/token
verify token in url is valid.
if valid enable user ,else show appropriate error  msg to user


signup_user(){
token=randon_token_generate()
saveTodb(user,token,System.currentTimestamp()/1000);
}

verify_user_token(username,token){
token_form_db = get_token-from_db(username)
if user is enabled{
return "user is already enabed"
}

if (token_form_db !=token){
return "invalid token"
}
day_secs=24*60*60
diff = Syatem.currentTimestamp()/1000 - created_time_from_db
if(dif<=day_secs){
enable user;
return "user registration succeded"
}
else{
return "token expired , please signup again"
}
}
