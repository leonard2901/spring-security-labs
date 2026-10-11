export type Instructor = {
  name: string;
};

export type TrainingSession = {
  version: number;
  location: string;
  topic: string;
  instructor: Instructor;
  description: string;
  modifiedOn: string | null;
  createdBy: string | null;
  modifiedBy: string | null;
  seats: number | null;
  internal_id: string;
};

export function emptyTrainingSession(): Partial<TrainingSession> {
  return {
    location  : '',
    topic     : '',
    instructor: { name: '' },
  };
}
