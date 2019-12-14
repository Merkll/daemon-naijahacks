import mongoose from 'mongoose';
import { config } from 'dotenv';
import User from './user';
import Contact from './contact';
import Message from './message';
import Location from './location';

config();

const connect = () => mongoose.connect(process.env.DATABASE_URL, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
  useCreateIndex: true,
  useFindAndModify: false,
});

const db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', () => {
  console.log("we're connected!");
});

const models = {
  User, Contact, Message, Location,
};

export { connect };

export default models;
