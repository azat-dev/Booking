import React from "react";
import PropsPageListings from "./props";

const PageListings = ({vm}: PropsPageListings) => {

    return (
        <div>
            <h1>Page Listings</h1>
        </div>
    );
}

export default React.memo(PageListings);