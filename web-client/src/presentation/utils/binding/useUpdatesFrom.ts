import { useEffect, useMemo, useReducer, useRef, useState } from "react";
import Subject from "./Subject";

const useUpdatesFrom = (...dependencies: Subject<any | null>[]) => {
    const [newValues, setNewValues] = useState(() =>
        dependencies.map((item) => item.value)
    );

    useEffect(() => {
        const subscriptions = dependencies.map((item, itemIndex) => {
            return item.listen((newValue) => {
                setNewValues((prev) => {
                    const newValues = [...prev];
                    newValues[itemIndex] = newValue;
                    return newValues;
                });
            });
        });

        setNewValues(dependencies.map((item) => item.value));
        return () => {
            subscriptions.forEach((s) => s.cancel());
        };
    }, dependencies);

    return newValues;
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
};