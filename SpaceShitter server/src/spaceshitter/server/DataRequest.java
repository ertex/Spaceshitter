package spaceshitter.server;
public class DataRequest {//Sure this migth not follow the naming convention to a 100%, but the name is adeqvate
//The purpose of this class is to relay information from the networkhandler to the main program so it can be sent back
//So in short, this class will have information for a get request
    
// DataRequest and spaceshitter.server belongs to David Johansson Te2
int networkID;//This is the identifier that the networkhanlder has, this is so the return goes to the correct handler
Object data; //this is the data that the package contains, depending on the type of data, the main class recognize it as different requests

public DataRequest (int networkID,Object data){
this.data = data;
this.networkID = networkID;
}

public Object getData(){
return data;
}

public int getNetworkID(){
return networkID;
}

}