import Listings from "../../../../domain/listings/entities/Listings.ts";
import RowVM from "./row/RowVM.ts";
import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";

class ListingsTableVM {

    public readonly rows: Subject<RowVM[]>;

    public constructor(private readonly listings: Listings) {

        this.rows = value([] as RowVM[]);
    }

    public load = async () => {
        const ownListings = await this.listings.getOwnListings();

        const newRows = ownListings.map((item) => {
            return new RowVM(
                item.id,
                item.title,
                `${item.address.country}, ${item.address.city}, ${item.address.street}`,
                item.status
            );
        });

        this.rows.set(newRows);
    }
}

export default ListingsTableVM;