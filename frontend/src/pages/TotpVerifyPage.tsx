import { useMutation } from "@tanstack/react-query";
import axios, { AxiosError } from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import "./TotpVerifyPage.css";
import { useUser } from "../context/useUser";


function TotpVerifyPage() {
    const [totpCode, setTotpCode] = useState("");
    const navigate = useNavigate();
    const {refreshUser} = useUser();

    // Define the mutation for TOTP verification
    const verifyTotpMutation = useMutation({
        mutationFn: async (code: string) => {
            const response = await axios.post("http://localhost:8080/totp-verify", { code }, { withCredentials: true, withXSRFToken: true });
            return response.data;
        },
        onSuccess: (verified: boolean) => {
            if (!verified) {
                toast.error("Code verification failed, please try again.");
            }
            else {
                refreshUser();
                toast.success("2FA verification successful!");
                navigate("/dashboard", { replace: true });
            }

        },
        onError: (error: AxiosError<{ message: string }>) => {
            const message =
                error.response?.data?.message || "Something went wrong. Please try again.";
            toast.error(message);
        },
    });

    // Handle form submission
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!totpCode.trim()) {
            toast.error("Please enter the verification code.");
            return;
        }
        verifyTotpMutation.mutate(totpCode);
    };

    return (
        <div className="totp-verify-page">
            <h1>Verify Your Identity</h1>
            <p>Please enter the 6-digit code from your authenticator app.</p>
            <form onSubmit={handleSubmit} className="totp-verify-form">
                <input
                    type="text"
                    value={totpCode}
                    onChange={(e) => setTotpCode(e.target.value)}
                    placeholder="Enter TOTP Code"
                    maxLength={6}
                    className="totp-input"
                />
                <button
                    type="submit"
                    className="submit-button"
                >
                    Verify
                </button>
            </form>
        </div>
    );
}

export default TotpVerifyPage;
