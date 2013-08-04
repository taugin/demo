package android.os;
/** 
    {@hide} 
*/  
interface IHardwareService
{  
    boolean getFlashlightEnabled();  
    void setFlashlightEnabled(boolean on);  
}