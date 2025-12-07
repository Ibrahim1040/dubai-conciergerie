export interface Property {
  id?: number;

  title: string;
  city: string;
  address: string;

  capacity: number;
  rentalType: 'SHORT_TERM' | 'LONG_TERM' | string;

  nightlyPrice?: number | null;
  monthlyPrice?: number | null;

  ownerId?: number | null;
}
