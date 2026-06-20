import type { Severity } from './data'

export const severityStyles: Record<
  Severity,
  { dot: string; badge: string; text: string; bar: string; ring: string }
> = {
  critical: {
    dot: 'bg-critical',
    badge: 'bg-critical/12 text-critical border-critical/25',
    text: 'text-critical',
    bar: 'bg-critical',
    ring: 'ring-critical/40',
  },
  warning: {
    dot: 'bg-warning',
    badge: 'bg-warning/15 text-warning border-warning/30',
    text: 'text-warning',
    bar: 'bg-warning',
    ring: 'ring-warning/40',
  },
  moderate: {
    dot: 'bg-info',
    badge: 'bg-info/12 text-info border-info/25',
    text: 'text-info',
    bar: 'bg-info',
    ring: 'ring-info/40',
  },
  healthy: {
    dot: 'bg-healthy',
    badge: 'bg-healthy/12 text-healthy border-healthy/25',
    text: 'text-healthy',
    bar: 'bg-healthy',
    ring: 'ring-healthy/40',
  },
}

/** Returns a token class for an indicator value (higher = better). */
export function valueTone(value: number): {
  text: string
  bar: string
} {
  if (value < 35) return { text: 'text-critical', bar: 'bg-critical' }
  if (value < 55) return { text: 'text-warning', bar: 'bg-warning' }
  if (value < 70) return { text: 'text-info', bar: 'bg-info' }
  return { text: 'text-healthy', bar: 'bg-healthy' }
}
