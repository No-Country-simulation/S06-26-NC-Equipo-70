import * as React from "react";
import { cn } from "@/lib/utils";

function Page({ className, ...props }: React.ComponentProps<"div"> & {}) {
  return (
    <div
      data-slot="page"
      className={cn("mx-auto w-full px-4 py-6 md:px-6 lg:py-8 max-w-[1600px]", className)}
      {...props}
    />
  );
}

function PageHeader({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="page-header"
      className={cn(
        "flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between",
        className,
      )}
      {...props}
    />
  );
}

function PageTitle({ className, ...props }: React.ComponentProps<"h1">) {
  return (
    <h1
      data-slot="page-title"
      className={cn("text-2xl font-semibold tracking-tight text-balance", className)}
      {...props}
    />
  );
}

function PageDescription({ className, ...props }: React.ComponentProps<"p">) {
  return (
    <p
      data-slot="page-description"
      className={cn("mt-1 text-sm text-muted-foreground", className)}
      {...props}
    />
  );
}

function PageActions({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div data-slot="page-actions" className={cn("flex items-center gap-2", className)} {...props} />
  );
}

function PageContent({ className, ...props }: React.ComponentProps<"div">) {
  return <div data-slot="page-content" className={cn("mt-6", className)} {...props} />;
}

function PageFooter({ className, ...props }: React.ComponentProps<"div">) {
  return <div data-slot="page-footer" className={cn("mt-6", className)} {...props} />;
}

export { Page, PageHeader, PageTitle, PageDescription, PageActions, PageContent, PageFooter };
