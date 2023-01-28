авторизационный токен формируется по правилу
для пользователя 
```
userName = Misha
password = PWD
```
необходимо составить строку формата 
```
Misha:PWD
```
закодировать ее в base64 - 
```
 TWlzaGE6UFdE
```
итоговый токен будет формата - 
```
Authorization: Bearer TWlzaGE6UFdE
```