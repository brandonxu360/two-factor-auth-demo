import { useState, ReactNode, useEffect } from 'react';
import { UserContext } from './UserContext';
import { User } from '../types/User';
import { fetchUser } from '../utils/fetchUser';

/**
 * A provider that stores the current user and provides a function to set the user.
 * @param children The children to render
 * @returns The user provider
 */
export function UserProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    fetchUser().then((user) => setUser(user));
  }, []);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}
