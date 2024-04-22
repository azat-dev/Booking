import { useEffect, useMemo, useReducer } from "react";
import Subject from "./Subject";

const useUpdatesFrom = (...dependencies: Subject<any | null>[]) => {
    const [, forceUpdate] = useReducer((x) => x + 1, 0);

    useEffect(() => {
        const listener = () => forceUpdate();
        const subscriptions = dependencies.map((item) => item.listen(listener));

        return () => {
            subscriptions.forEach((s) => s.cancel());
        };
    }, dependencies);
    
    return dependencies.map((item) => item.value);
};

export default useUpdatesFrom;

export interface DestroyableViewModel {
    destroy: () => void;
}

export const useDestroyableViewModel = (vm: any) => {

    useEffect(() => {
        return () => {
            if (vm.destroy) {
                vm.destroy();
            }
        };
    }, [vm]);
}