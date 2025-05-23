import { useEffect } from "react";
import useToastStore from "../store/useToastStore";

const Toast=()=>{
    const visible=useToastStore(state=>state.visible);
    const message=useToastStore(state=>state.message);
    const type=useToastStore(state=>state.type);
    const hideToast=useToastStore(state=>state.hideToast);

    useEffect(()=>{
        console.log('visible',visible);
        if(visible){
            setTimeout(() => {
                hideToast();
            }, 5000);
        }
    },[visible])

    const styles= type==='SUCCESS'?"fixed top-4 right-4 z-50 p-4 rounded-md bg-green-600 text-white max-w-md":
    "fixed top-4 right-4 z-50 p-4 rounded-md bg-red-600 text-white max-w-md";

    return(
        <> {visible &&
            <div className={styles}>
                <span className="text-2xl">{message} </span>
            </div>}
        </>
    )
}
export default Toast;