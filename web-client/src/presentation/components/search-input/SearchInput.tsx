import React from "react";

import PropsSearchInput from "./props";
import style from "./style.module.scss";
import Autocomplete from "@mui/joy/Autocomplete";
import Box from "@mui/joy/Box";

import SearchRoundedIcon from "@mui/icons-material/SearchRounded";

const SearchInput = ({ vm }: PropsSearchInput) => {
    return (
        <div className={style.searchInput}>
            <Box
                sx={{
                    padding: 2,
                }}
            >
                <Autocomplete
                    startDecorator={<SearchRoundedIcon />}
                    options={[]}
                    size="lg"
                    placeholder="Search for a place to stay..."
                    variant="outlined"
                />
            </Box>
        </div>
    );
};

export default React.memo(SearchInput);
