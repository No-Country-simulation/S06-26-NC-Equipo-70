import { ChevronDown } from "lucide-react";
import { cn } from "@/lib/utils";

type Option = string | { label: string; value: string };

interface SelectFieldProps {
  label: string;
  value: string;
  options: Option[];
  onChange: (value: string) => void;
  placeholder?: string;
  disabled?: boolean;
  className?: string;
}

export function SelectField({
  label,
  value,
  options,
  onChange,
  placeholder,
  disabled = false,
  className,
}: SelectFieldProps) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-xs font-medium text-muted-foreground">{label}</span>
      <div className="relative">
        <select
          value={value}
          disabled={disabled}
          onChange={(e) => onChange(e.target.value)}
          className={cn(
            "h-9 w-full appearance-none rounded-xl border border-border bg-background px-3 pr-8 text-sm outline-none transition-colors hover:border-primary/40 focus:border-ring",
            className,
          )}
        >
          {placeholder && (
            <option value="" disabled>
              {placeholder}
            </option>
          )}
          {options.map((option) => {
            const value = typeof option === "string" ? option : option.value;
            const label = typeof option === "string" ? option : option.label;

            return (
              <option key={value} value={value}>
                {label}
              </option>
            );
          })}
        </select>
        <ChevronDown className="pointer-events-none absolute right-2.5 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
      </div>
    </label>
  );
}
