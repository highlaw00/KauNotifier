import { Box, TextField, Typography, FormControl, Grid, Button, useMediaQuery } from "@mui/material";
import { Send } from "@mui/icons-material";

const SubscribeTest = () => {
    const isMobile = useMediaQuery("(max-width: 767px)")
    const emailHelperText = "실제로 사용하는 이메일을 입력해주세요."
    const nameHelperText = "이름은 식별을 위해서만 사용되며, 실제 이름이 아니어도 됩니다."
    return (
        <Box sx={{
            minHeight: "95vh"
        }}>
            <Typography variant="h4" marginY={2}>구독 신청</Typography>
            <FormControl sx={{minWidth: "100%"}}>
                <Grid container justifyContent="center" alignItems="center" textAlign="center" spacing={1}>
                    <Grid item xs={9}>
                        <TextField fullWidth label="이메일" helperText={emailHelperText}>이메일</TextField>
                    </Grid>
                    <Grid item xs={3} paddingBottom={3}>
                        <Button startIcon={isMobile ? null : <Send/>}>코드 전송</Button>
                    </Grid>
                    <Grid item xs={12}>
                        <TextField fullWidth label="이름" helperText={nameHelperText}>이름</TextField>
                    </Grid>
                </Grid>
            </FormControl>
        </Box>
    )
}

export default SubscribeTest;