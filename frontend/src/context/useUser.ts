import { useContext } from "react";
import { UserContext } from "./UserContext";

/**
 * A hook that returns the current user and a function to set the user.
 * @returns The current user and a function to set the user
 */
export function useUser() {
    const context = useContext(UserContext);
    if (context === undefined) {
      throw new Error('useUser must be used within a UserProvider');
    }
    return context;
  }