REM generate public/private key, usually RSA, but could be DSA.
REM Modern certs are mostly RSA, but DSA certs work in Java 1.2.
REM set up distinguished name as your Certificate Authority requests.
REM keytool -genkey -keyalg RSA -alias IOLMACertificate -dname "CN=iolma.com, OU=Radu Obreja, O=Telecast Systems S.R.L., L=Bucharest, C=RO"

REM export cert request
REM keytool -certreq -alias IOLMCertificate -file certrequest.csr

REM --------------------------------------------------------------------------

REM generate a phony self-signed certificate

REM -keyalg DSA for JDK 1.2 compatibility, private-public pair.
REM However RSA is what most modern certs use.
REM see http://docs.sun.com/source/816-5539-10/app_dn.htm for construction of distinguished name
REM create private/public key pair
"C:\Program Files\Java\jdk1.7.0_21\bin\keytool" -genkey -keystore .keystore -keyalg RSA -alias TELECAST_Certificate -dname "CN=telecast.ro, OU=Radu Obreja, O=Telecast Systems S.R.L., L=Bucharest, C=RO" -validity 999

REM generate the self-signed certificate containing public key
"C:\Program Files\Java\jdk1.7.0_21\bin\keytool" -selfcert -keystore .keystore -alias TELECAST_Certificate -validity 999

REM export the self-signed certificate in x.509 printable format, public key only.
"C:\Program Files\Java\jdk1.7.0_21\bin\keytool" -export -keystore .keystore -alias TELECAST_Certificate -rfc -file TELECAST_Certificate.cer

