import RowVM from "./row/RowVM.ts";
import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";
import VM from "../../../utils/VM.ts";


export interface ListingsTableVMLoadedListing {
    id: string;
    title: string;
    address: {
        country: string;
        city: string;
        street: string;
    };
    status: string;
}

class ListingsTableVM extends VM {

    public readonly rows: Subject<RowVM[]>;

    public delegate!: {
        loadListings: () => void;
    };

    public constructor() {
        super();
        this.rows = value([] as RowVM[]);
    }

    public load = async () => {
        this.delegate.loadListings;
    }

    public displayLoadedListings = (listings: ListingsTableVMLoadedListing[]) => {
        const newRows = listings.map((item) => {
            return new RowVM(
                item.id,
                item.title,
                `${item.address.country}, ${item.address.city}, ${item.address.street}`,
                item.status
            );
        });

        this.rows.set(newRows);
    }

    public displayFailedLoadListings = () => {

    }
}

export default ListingsTableVM;