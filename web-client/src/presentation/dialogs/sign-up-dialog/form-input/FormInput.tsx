import React from "react";

import PropsFormInput from "./props";
import style from "./style.module.scss";
import FormControl from "@mui/joy/FormControl";
import FormLabel from "@mui/joy/FormLabel";
import Input from "@mui/joy/Input";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import FormHelperText from "@mui/joy/FormHelperText";
import InfoOutlined from "@mui/icons-material/InfoOutlined";

const FormInput = ({ vm, label, type, placeholder }: PropsFormInput) => {
    const [text, isDisabled, isWrong, errorText] = useUpdatesFrom(
        vm.text,
        vm.isDisabled,
        vm.isWrong,
        vm.errorText
    );

    return (
        <FormControl disabled={isDisabled} error={isWrong}>
            <FormLabel>{label}</FormLabel>
            <Input
                size="lg"
                type={text}
                placeholder={placeholder}
                value={text}
                onChange={(event) => vm.change(event.target.value)}
                error={isWrong}
            />
            <FormHelperText sx={{ opacity: errorText ? 1 : 0, fontSize: 12 }}>
                <InfoOutlined sx={{width: 15, height: 15}}/>
                {errorText}
            </FormHelperText>
        </FormControl>
    );
};

export default React.memo(FormInput);
