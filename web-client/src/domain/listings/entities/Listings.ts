import Listing from "./Listing.ts";

class Listings {

    public getOwnListings = async (): Promise<Listing[]> => {
        return [
            new Listing("1", "Title 1", {city: "City 1", country: "Country 1", street: "street1"}, "PUBLISHED"),
            new Listing("2", "Title 2", {city: "City 2", country: "Country 2", street: "street2"}, "PUBLISHED"),
        ];
    }

    public getListing = async (id: string): Promise<Listing> => {
        return new Listing("1", "Title 1", {city: "City 1", country: "Country 1", street: "street1"}, "PUBLISHED")
    }

    public addNewListing = async (title: string, address: {city: string, country: string, street: string}): Promise<void> => {
        console.log("Adding new listing with title: ", title, " and address: ", address);
    }
}

export default Listings;