import RowVM from "./row/RowVM.ts";
import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";
import VM from "../../../utils/VM.ts";
import {ListingPrivateDetails} from "../../../../data/api/listings";
import ListingId from "../../../../domain/listings/values/ListingId.ts";


class ListingsTableVM extends VM {

    public readonly rows: Subject<RowVM[]>;

    public constructor(
        private readonly loadListings: () => Promise<ListingPrivateDetails[]>,
        private readonly openListing: (id: ListingId) => void
    ) {
        super();
        this.rows = value([] as RowVM[]);
    }

    public load = async () => {
        const listings = await this.loadListings();

        const newRows = listings.map((item) => {
            return new RowVM(
                item.id,
                item.title,
                item.address && `${item.address.country}, ${item.address.city}, ${item.address.street}`,
                item.status,
                () => {
                    this.openListing(
                        new ListingId(item.id)
                    )
                }
            );
        });

        this.rows.set(newRows);
    }
}

export default ListingsTableVM;