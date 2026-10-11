export type Page<T> = {
  totalElements: number;
  totalPages: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  last: boolean;
  size: number;
  content: T[];
};
