import React, {useEffect} from "react";
import PropsDescriptionEditor from "./props";
import {Textarea, Typography} from "@mui/joy";
import Box from "@mui/joy/Box";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom.ts";
import {desktop, mobile, tablet} from "../../../utils/selectors.ts";

const DescriptionEditor = ({vm}: PropsDescriptionEditor) => {

    const [description, numberOfCharacters] = useUpdatesFrom(vm.description, vm.numberOfCharacters);

    useEffect(() => {
        vm.load();
    }, [vm]);

    return (
        <Box
            sx={(theme) => ({
                [mobile(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'flex-start',
                    gap: '10px',
                    padding: 3
                },
                [desktop(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px'
                },
                [tablet(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px'
                },
            })}
        >

            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    maxWidth: 500
                }}
            >
                <Typography level='h1'>
                    Create a description
                </Typography>
                <Typography>
                    Describe your space, what makes it unique, and what guests can expect
                </Typography>

                <br/>
                <Textarea
                    minRows={7}
                    size='lg'
                    value={description}
                    onChange={vm.onChange}
                    color={numberOfCharacters > vm.maxNumberOfCharacters ? 'danger' : 'neutral'}
                >

                </Textarea>
                <Typography
                    level='body-sm'
                    onChange={vm.onChange}
                    color={numberOfCharacters > vm.maxNumberOfCharacters ? 'danger' : 'neutral'}
                >
                    {numberOfCharacters}/{vm.maxNumberOfCharacters} characters
                </Typography>
            </Box>
        </Box>
    );
}

export default React.memo(DescriptionEditor);