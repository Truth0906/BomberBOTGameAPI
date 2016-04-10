#ifndef __GAMEAPI_H_
#define __GAMEAPI_H_

#include <winsock2.h>

#pragma  comment(lib,"ws2_32.lib")

#ifdef __cplusplus
extern "C" {
#endif
	
	int connectServer(char *serverIP,  int Port, SOCKET *sclient);
	int disConnectServer(SOCKET sclient );
	int echo( SOCKET sclient, char *msg, char **response);
	int HexDecode(char *strHex, unsigned char **data, int *ldata);


#ifdef __cplusplus
}
#endif

	
#endif 