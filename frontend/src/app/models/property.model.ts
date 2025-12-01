export interface Property {
  id: number;
  title: string;
  city: string;
  address: string;
  capacity: number;
  nightlyPrice: number;
  monthlyPrice: number;
  rentalType: 'SHORT_TERM' | 'LONG_TERM';
}
