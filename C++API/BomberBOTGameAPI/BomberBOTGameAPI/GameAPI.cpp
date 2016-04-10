#include <stdio.h>
#include "define.h"
#include "GameAPI.h"

int CharToInt(char *str)
{
	int value = 0;
	if(str){
		value = 16*str[0] + str[1];
	}
	return value ;
}

int HexDecode(char *strHex, unsigned char **data, int *ldata)
{		
		unsigned char *pdata = NULL;
		int dataLen = 0;
		int rtn = 5001;
		int i = 0;

		if(strHex == NULL || strlen(strHex) == 0){
			rtn = 	5005;
			goto end;
		}
		dataLen = (int)strlen(strHex);
		pdata = (unsigned char*)calloc(dataLen/2+1, sizeof(unsigned char));
  
		if(pdata == NULL) {
			rtn = 5002;
			goto end;
		}
	
		for(i=0;i<dataLen/2;i++){			
			char byte[3] = {0};
			strncpy(byte,(const char*)strHex+2*i,2);
			sprintf((char*)(pdata+i),"%c",CharToInt(byte));
		}

	*data = pdata;
    *ldata = dataLen/2;
		
end:

	 return 0;
}

int connectServer(char *serverIP,  int Port, SOCKET *sclient)
{
	int  rtn = -1;

	 WORD sockVersion = MAKEWORD(2,2);
	 WSADATA data  = {0}; 

	 if(!serverIP || !strlen(serverIP)) goto end;
	 if(!sclient) goto end;

	 rtn = WSAStartup(sockVersion, &data) ;
	 if(rtn != 0) goto end;

	  *sclient = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
     if(*sclient == INVALID_SOCKET){
         printf("invalid socket !");
         goto end;
     }

	  sockaddr_in serAddr;
     serAddr.sin_family = AF_INET;
     serAddr.sin_port = htons(Port);
     serAddr.sin_addr.S_un.S_addr = inet_addr(serverIP); 

	 rtn = connect(*sclient, (sockaddr *)&serAddr, sizeof(serAddr)) ;
	 if(rtn == SOCKET_ERROR){
	
		 for(int i=0;i<(sizeof(PortList)/sizeof(int));i++){
				serAddr.sin_port = htons(PortList[i]);
				rtn = connect(*sclient, (sockaddr *)&serAddr, sizeof(serAddr)) ;
				if(rtn == 0) break;
		 }

         printf("connect error !\n");
         closesocket(*sclient);
		 *sclient = NULL;	 
	 }

end:

	return rtn;
}

int disConnectServer(SOCKET sclient )
{
    closesocket(sclient);
     WSACleanup();

	return 0;
}

int echo( SOCKET sclient, char *msg)
{
	 int rec = -1, i =0;
	 int datalen = 0;
	 char * data = "{\"functionname\":\"6563686F\",\"message\":\"\"}\r\n";
	 char *compose = NULL;
	 char *hex = NULL;
	 char recData[255]={0};

	 datalen += (int)strlen(data) + 1;
	 if(msg){
			datalen  += 2*(int)strlen(msg) ;		
			 hex = (char*)calloc(2*strlen(msg)+1,sizeof(char));
			 for(i=0;i<(int)strlen(msg);i++)
				 sprintf(hex+2*i,"%02X",msg[i]);
			compose = (char*)calloc(datalen,sizeof(char));
			sprintf(compose,"{\"functionname\":\"6563686F\",\"message\":%s\"\"}\r\n",hex);
	 }		
	 else{
			compose = (char*)calloc(datalen,sizeof(char));
			sprintf(compose,data);
	 }

	 send(sclient, compose, (int)strlen(compose), 0);

      rec = recv(sclient, recData, 254, 0);
     if(rec > 0){
         recData[rec] = 0x00;
         printf(recData);
     }

	if(compose) free(compose);
	if(hex) free(hex);

	return 0;
}