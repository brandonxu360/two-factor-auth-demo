import { createContext } from "react";
import { User } from "../types/User";

// The UserContextType interface defines the shape of the UserContext object.
interface UserContextType {
    user: User | null;
    setUser: (user: User | null) => void;
  }
  
  export const UserContext = createContext<UserContextType | undefined>(undefined);