import {useEffect, useState} from "react";

const useLoadedValue = (getValue: () => Promise<any>, dependencies: any[]): any => {
    const [value, setValue] = useState(null);
    useEffect(() => {
        getValue().then(setValue);
    }, dependencies);
    return value;
}

export default useLoadedValue;