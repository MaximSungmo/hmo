```
--- github ssh ----------------------------------------------------------------

1. ssh-key 생성


[root@sunnyvale ~]#cd
[root@sunnyvale ~]# pwd
/root
[root@sunnyvale ~]# ssh-keygen
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa): 
Created directory '/root/.ssh'.
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /root/.ssh/id_rsa.
Your public key has been saved in /root/.ssh/id_rsa.pub.
The key fingerprint is:
1a:ff:03:f2:eb:d1:8a:db:41:a7:29:be:cd:d6:7b:1c root@sunnyvale.hellomyoffice.com
The key's randomart image is:
+--[ RSA 2048]----+
|                 |
|                 |
|                 |
|                 |
|      . S .      |
|      .=.=  E    |
|      oo*o.. .   |
|     . *o=o o    |
|      ==B.o+     |
+-----------------+


2. id_rsa.pub 내용 확인
[root@sunnyvale ~]# cat .ssh/id_rsa.pub 
ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAlXcuirMhZCNTROYOah5aS7RMh8Idtq4igQGrZdZ5VqNDf/4htqBdkLvK+ZAz07UIDYtCTDMoRgH2Y9Bg6/1mFnN51eJ5m8FYPYEGDZVUHKuLZ7w6XvgcgXCwYuZKUq0nbHtpDAx4xvgDQqtarbxvU8q65OBJu1pF4aHngKQ/cmCn9ug3fCP82ihGpGtXxeOEdkQ3wIQIJW76Uin7i+du3u8Yqg83TZ01FuyzDRVA+HgkdEPamIeghT1ka8O4vQpFMoEQzA5L4YRGG+lyR/Lhv6/kH2WyEN57GcervpEYRxK/BVOjL6E5swXPXgRYm3/cAOxMz7cM7x4UvlSNXNhUzQ== root@sunnyvale.hellomyoffice.com


3. github 등록
   settings > ssh keys 에서 add ssh key
   id_rsa.pub 내용 복사/붙혀넣기


4. 테스트 
[root@sunnyvale .ssh]# ssh git@github.com
Warning: Permanently added the RSA host key for IP address '192.30.252.129' to the list of known hosts.
PTY allocation request failed on channel 0
Hi kickscar! You've successfully authenticated, but GitHub does not provide shell access.


                                                                                         Connection to github.com closed.
                                                                                         
                                                                                         
                                                                                         
                                                                                         
--- git linux 설치 -------------------------------------------------------------------

1.Git에서 필요한 라이브러리 다운로드 또는 확인
yum -y install zlib-devel openssl-devel cpio expat-devel gettext-devel

2.Git 소스 다운로드
http://git-core.googlecode.com 에서 최신 소스 확인
wget http://git-core.googlecode.com/files/git-1.9.0.tar.gz

3. 컴파일 / 인스톨
tar zxvf git-1.9.0.tar.gz
cd git-1.9.0 
./configure
make
make install

4. 확인
git --version

5. jenkins 에서 github plugin 설치
6. jenkins 설정에서 git 결로 지정해 줄 것

```
