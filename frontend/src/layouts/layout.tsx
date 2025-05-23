import Header from "../components/header";
import Footer from "../components/footer";
interface Props {
  children: React.ReactNode;
}
const Layout = ({ children }: Props) => {
  return (
    <>
      <div className="flex flex-col min-h-screen mt-8">
        <Header />
        <div className="container mx-auto py-6 flex-1">{children}</div>
        <Footer />
      </div>
    </>
  );
};
export default Layout;
