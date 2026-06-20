export type Severity = 'critical' | 'warning' | 'moderate' | 'healthy'

export type IndicatorKey =
  | 'digitalInequality'
  | 'employment'
  | 'connectivity'
  | 'training'
  | 'mentorship'
  | 'mentalHealth'

export interface Indicator {
  key: IndicatorKey
  label: string
  /** 0-100, higher = better coverage/outcome */
  value: number
}

export interface Region {
  id: string
  name: string
  district: string
  /** normalized map coordinates 0-100 */
  x: number
  y: number
  population: number
  /** 0-100, higher = worse / more risk */
  riskScore: number
  severity: Severity
  networkCoverage: number
  employmentRate: number
  trainingPrograms: number
  mentorshipCoverage: number
  mentalHealthAccess: number
  digitalAccess: number
  indicators: Indicator[]
}

export const SEVERITY_META: Record<
  Severity,
  { label: string; token: string; range: string }
> = {
  critical: { label: 'Critical', token: 'critical', range: '80–100 risk' },
  warning: { label: 'High', token: 'warning', range: '60–79 risk' },
  moderate: { label: 'Moderate', token: 'info', range: '40–59 risk' },
  healthy: { label: 'Stable', token: 'healthy', range: '0–39 risk' },
}

function buildIndicators(r: {
  digitalAccess: number
  employmentRate: number
  networkCoverage: number
  trainingPrograms: number
  mentorshipCoverage: number
  mentalHealthAccess: number
}): Indicator[] {
  return [
    { key: 'digitalInequality', label: 'Digital access', value: r.digitalAccess },
    { key: 'employment', label: 'Employment', value: r.employmentRate },
    { key: 'connectivity', label: 'Connectivity', value: r.networkCoverage },
    { key: 'training', label: 'Training programs', value: r.trainingPrograms },
    { key: 'mentorship', label: 'Mentorship', value: r.mentorshipCoverage },
    { key: 'mentalHealth', label: 'Mental health', value: r.mentalHealthAccess },
  ]
}

function severityFromRisk(risk: number): Severity {
  if (risk >= 80) return 'critical'
  if (risk >= 60) return 'warning'
  if (risk >= 40) return 'moderate'
  return 'healthy'
}

const seed: Omit<Region, 'severity' | 'indicators'>[] = [
  {
    id: 'norte-alto',
    name: 'Norte Alto',
    district: 'Northern Highlands',
    x: 28,
    y: 16,
    population: 184500,
    riskScore: 91,
    networkCoverage: 34,
    employmentRate: 41,
    trainingPrograms: 18,
    mentorshipCoverage: 12,
    mentalHealthAccess: 22,
    digitalAccess: 29,
  },
  {
    id: 'vale-seco',
    name: 'Vale Seco',
    district: 'Western Valley',
    x: 16,
    y: 44,
    population: 96200,
    riskScore: 86,
    networkCoverage: 38,
    employmentRate: 46,
    trainingPrograms: 21,
    mentorshipCoverage: 19,
    mentalHealthAccess: 26,
    digitalAccess: 33,
  },
  {
    id: 'serra-leste',
    name: 'Serra Leste',
    district: 'Eastern Ridge',
    x: 72,
    y: 24,
    population: 142800,
    riskScore: 78,
    networkCoverage: 49,
    employmentRate: 53,
    trainingPrograms: 34,
    mentorshipCoverage: 28,
    mentalHealthAccess: 31,
    digitalAccess: 44,
  },
  {
    id: 'baixada-sul',
    name: 'Baixada Sul',
    district: 'Southern Lowlands',
    x: 44,
    y: 78,
    population: 268400,
    riskScore: 74,
    networkCoverage: 52,
    employmentRate: 55,
    trainingPrograms: 30,
    mentorshipCoverage: 33,
    mentalHealthAccess: 38,
    digitalAccess: 47,
  },
  {
    id: 'porto-novo',
    name: 'Porto Novo',
    district: 'Coastal Port',
    x: 84,
    y: 62,
    population: 312600,
    riskScore: 63,
    networkCoverage: 61,
    employmentRate: 64,
    trainingPrograms: 44,
    mentorshipCoverage: 41,
    mentalHealthAccess: 46,
    digitalAccess: 58,
  },
  {
    id: 'campo-verde',
    name: 'Campo Verde',
    district: 'Central Plains',
    x: 50,
    y: 48,
    population: 205300,
    riskScore: 57,
    networkCoverage: 66,
    employmentRate: 67,
    trainingPrograms: 49,
    mentorshipCoverage: 47,
    mentalHealthAccess: 52,
    digitalAccess: 63,
  },
  {
    id: 'lago-azul',
    name: 'Lago Azul',
    district: 'Lakeside',
    x: 34,
    y: 64,
    population: 88700,
    riskScore: 48,
    networkCoverage: 72,
    employmentRate: 70,
    trainingPrograms: 55,
    mentorshipCoverage: 53,
    mentalHealthAccess: 58,
    digitalAccess: 69,
  },
  {
    id: 'cidade-central',
    name: 'Cidade Central',
    district: 'Metropolitan Core',
    x: 60,
    y: 40,
    population: 524900,
    riskScore: 31,
    networkCoverage: 88,
    employmentRate: 79,
    trainingPrograms: 72,
    mentorshipCoverage: 68,
    mentalHealthAccess: 74,
    digitalAccess: 85,
  },
  {
    id: 'jardim-norte',
    name: 'Jardim Norte',
    district: 'Northern Suburb',
    x: 56,
    y: 20,
    population: 167200,
    riskScore: 27,
    networkCoverage: 90,
    employmentRate: 81,
    trainingPrograms: 76,
    mentorshipCoverage: 71,
    mentalHealthAccess: 78,
    digitalAccess: 88,
  },
  {
    id: 'praia-mar',
    name: 'Praia Mar',
    district: 'Seafront',
    x: 90,
    y: 40,
    population: 121500,
    riskScore: 22,
    networkCoverage: 92,
    employmentRate: 84,
    trainingPrograms: 80,
    mentorshipCoverage: 77,
    mentalHealthAccess: 82,
    digitalAccess: 91,
  },
]

export const regions: Region[] = seed.map((r) => ({
  ...r,
  severity: severityFromRisk(r.riskScore),
  indicators: buildIndicators(r),
}))

export function getRegion(id: string): Region | undefined {
  return regions.find((r) => r.id === id)
}

export const INDICATOR_OPTIONS: { key: IndicatorKey; label: string }[] = [
  { key: 'digitalInequality', label: 'Digital inequality' },
  { key: 'employment', label: 'Employment' },
  { key: 'connectivity', label: 'Connectivity' },
  { key: 'training', label: 'Training programs' },
  { key: 'mentorship', label: 'Mentorship coverage' },
  { key: 'mentalHealth', label: 'Mental health access' },
]

export const SERVICE_OPTIONS = [
  'All services',
  'Connectivity',
  'Workforce',
  'Education',
  'Mentorship',
  'Mental health',
]

export const DISTRICT_OPTIONS = [
  'All regions',
  ...Array.from(new Set(regions.map((r) => r.district))),
]

/* ---------- Aggregate KPIs ---------- */
const total = regions.length
const avg = (fn: (r: Region) => number) =>
  Math.round(regions.reduce((s, r) => s + fn(r), 0) / total)

export const kpis = {
  monitoredRegions: total,
  population: regions.reduce((s, r) => s + r.population, 0),
  criticalRegions: regions.filter((r) => r.severity === 'critical').length,
  atRiskRegions: regions.filter(
    (r) => r.severity === 'critical' || r.severity === 'warning',
  ).length,
  avgConnectivity: avg((r) => r.networkCoverage),
  avgEmployment: avg((r) => r.employmentRate),
  avgDigitalAccess: avg((r) => r.digitalAccess),
  avgMentalHealth: avg((r) => r.mentalHealthAccess),
  trainingCoverage: avg((r) => r.trainingPrograms),
  mentorshipCoverage: avg((r) => r.mentorshipCoverage),
}

export const coverageMetrics: {
  key: IndicatorKey
  label: string
  value: number
  target: number
}[] = [
  { key: 'connectivity', label: 'Network connectivity', value: kpis.avgConnectivity, target: 85 },
  { key: 'employment', label: 'Employment rate', value: kpis.avgEmployment, target: 75 },
  { key: 'training', label: 'Training programs', value: kpis.trainingCoverage, target: 70 },
  { key: 'mentorship', label: 'Mentorship coverage', value: kpis.mentorshipCoverage, target: 65 },
  { key: 'mentalHealth', label: 'Mental health access', value: kpis.avgMentalHealth, target: 80 },
  { key: 'digitalInequality', label: 'Digital access', value: kpis.avgDigitalAccess, target: 85 },
]

export function formatPopulation(n: number): string {
  if (n >= 1_000_000) return `${(n / 1_000_000).toFixed(1)}M`
  if (n >= 1_000) return `${(n / 1_000).toFixed(0)}K`
  return `${n}`
}

export interface AiSource {
  id: string
  title: string
  org: string
  year: string
  type: 'Census' | 'Survey' | 'Registry' | 'Report'
}

export const aiSources: AiSource[] = [
  { id: 's1', title: 'National Digital Inclusion Survey', org: 'Institute of Statistics', year: '2024', type: 'Survey' },
  { id: 's2', title: 'Regional Employment Registry', org: 'Ministry of Labor', year: '2024', type: 'Registry' },
  { id: 's3', title: 'Broadband Coverage Atlas', org: 'Telecom Authority', year: '2023', type: 'Report' },
  { id: 's4', title: 'Public Mental Health Census', org: 'Department of Health', year: '2024', type: 'Census' },
]

export const suggestedPrompts = [
  'Which regions have the highest digital inequality?',
  'Where should we prioritize new training programs?',
  'Compare connectivity gaps across northern districts',
  'Summarize mental health access risks for at-risk regions',
]
