const InstructionCards = () => {
  const steps = [
    {
      title: "1. Add Your Subscriptions",
      description:
        "Manually or automatically input your recurring subscriptions.",
      image: "/pluspointphotoshoot.jpg",
    },
    {
      title: "2. Set Reminders",
      description: "Get notified a day before any subscription renews.",
      image: "/pluspointphotoshoot.jpg",
    },
    {
      title: "3. Stay In Control",
      description: "Cancel or manage subscriptions from one dashboard.",
      image: "/pluspointphotoshoot.jpg",
    },
  ];

  return (
    <>
      <section className="grid md:grid-cols-3 gap-6 mt-16">
        {steps.map((step) => (
          <div
            key={step.title}
            className="bg-blue-100 p-6 rounded-2xl shadow-md text-center"
          >
            <img src={step.image} alt={step.title} className="mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">{step.title}</h3>
            <p className="text-gray-600">{step.description}</p>
          </div>
        ))}
      </section>
    </>
  );
};
export default InstructionCards;
