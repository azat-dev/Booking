import React, {useEffect} from "react";
import PropsTitleEditor from "./props";
import {Textarea, Typography} from "@mui/joy";
import Box from "@mui/joy/Box";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom.ts";
import {desktop, mobile, tablet} from "../../../utils/selectors.ts";

const TitleEditor = ({vm}: PropsTitleEditor) => {

    const [title, numberOfCharacters] = useUpdatesFrom(vm.title, vm.numberOfCharacters);

    useEffect(() => {
        vm.load();
    }, []);

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
                    Now, let's give your listing a title
                </Typography>
                <Typography>
                    A great title will help guests find your listing
                </Typography>

                <br/>
                <Textarea
                    minRows={7}
                    size='lg'
                    value={title}
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

export default React.memo(TitleEditor);