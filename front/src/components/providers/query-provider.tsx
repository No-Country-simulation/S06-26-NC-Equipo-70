"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { useState } from "react";

export function QueryProvider({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            retry: 1,
            staleTime: 1000 * 60 * 5, // 5 min
            refetchOnWindowFocus: false,
          },
        },
      }),
  );

  return (
    <>
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </>
  );
}
