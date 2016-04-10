// TestBomberBOTGameAPI.cpp : 定義主控台應用程式的進入點。
//

#include "stdafx.h"
#include "GameAPI.h"

int _tmain(int argc, _TCHAR* argv[])
{
	int rtn = -1;
	char *receive = NULL;
	SOCKET sclient  = {0};

	rtn = connectServer("127.0.0.1", 52013, &sclient);
	printf("%s %d rtn %d\n",__FILE__,__LINE__,rtn);

	rtn = echo(sclient, "Test String 123",&receive);
	printf("%s %d rtn %d\n receive %s",__FILE__,__LINE__,rtn,receive);

	disConnectServer(sclient);

	if(receive) free(receive);

	return 0;
}

