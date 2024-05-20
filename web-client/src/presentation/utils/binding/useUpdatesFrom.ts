import {useEffect, useState} from "react";
import {ReadonlySubject} from "./Subject";

const useUpdatesFrom = (...dependencies: ReadonlySubject<any | null>[]) => {
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

export interface DestroyableVM {
    destroy: () => void;
}

export const useDestroyableVM = (vm: any) => {
    useEffect(() => {
        return () => {
            if (vm.destroy) {
                vm.destroy();
            }
        };
    }, [vm]);
};
